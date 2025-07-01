package com.agendador.api_agendador.service;

import com.agendador.api_agendador.entity.Appointment;
import com.agendador.api_agendador.entity.Assistant;
import com.agendador.api_agendador.entity.DoctorSchedule;
import com.agendador.api_agendador.entity.Patient;
import com.agendador.api_agendador.entity.enums.AppointmentStatus;
import com.agendador.api_agendador.entity.enums.DayOfWeek;
import com.agendador.api_agendador.messaging.producer.AppointmentProducer;
import com.agendador.api_agendador.repository.AppointmentRepository;
import com.agendador.api_agendador.repository.AssistantRepository;
import com.agendador.api_agendador.repository.DoctorScheduleRepository;
import com.agendador.api_agendador.repository.PatientRepository;
import com.agendador.api_agendador.util.AppointmentConstants;
import com.agendador.api_agendador.web.dto.appointment.AppointmentResponseDTO;
import com.agendador.api_agendador.web.exception.BadRequestException;
import com.agendador.api_agendador.web.exception.ResourceAlreadyExistsException;
import com.agendador.api_agendador.web.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.agendador.api_agendador.util.AppointmentConstants.*;
import static com.agendador.api_agendador.util.AssistantConstants.ASSISTANT_ID;
import static com.agendador.api_agendador.util.AssistantConstants.freshAssistantEntity;
import static com.agendador.api_agendador.util.DoctorScheduleConstants.freshDoctorScheduleEntity;
import static com.agendador.api_agendador.util.PatientConstants.PATIENT_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    @InjectMocks
    private AppointmentService appointmentService;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private AssistantRepository assistantRepository;

    @Mock
    private DoctorScheduleRepository doctorScheduleRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private AppointmentProducer appointmentProducer;

    @Test
    void findById_ShouldReturnAppointmentResponseDTO_WhenFound() {
        Appointment appointment = freshAppointmentEntity();

        when(appointmentRepository.findById(APPOINTMENT_ID)).thenReturn(Optional.of(appointment));

        AppointmentResponseDTO response = appointmentService.findById(APPOINTMENT_ID);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(APPOINTMENT_ID);
        verify(appointmentRepository).findById(APPOINTMENT_ID);
    }

    @Test
    void findById_ShouldThrowResourceNotFoundException_WhenNotFound() {
        when(appointmentRepository.findById(APPOINTMENT_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> appointmentService.findById(APPOINTMENT_ID));

        verify(appointmentRepository).findById(APPOINTMENT_ID);
    }

    @Test
    void findAppointmentsByFilters_ShouldReturnPageOfAppointmentResponseDTO_WhenSuccessful() {
        Pageable pageable = PageRequest.of(0, 10);
        Appointment appointment = freshAppointmentEntity();
        Page<Appointment> page = new PageImpl<>(List.of(appointment), pageable, 1);

        when(appointmentRepository.findAll(
                any(Specification.class),
                eq(pageable)))
                .thenReturn(page);

        Page<AppointmentResponseDTO> responsePage = appointmentService.findAppointmentsByFilters(
                1L, 1L, 1L, AppointmentConstants.APPOINTMENT_STATUS,
                LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1), pageable);

        assertThat(responsePage).isNotNull();
        assertThat(responsePage.getTotalElements()).isEqualTo(1);
        assertThat(responsePage.getContent().get(0).id()).isEqualTo(AppointmentConstants.APPOINTMENT_ID);

        verify(appointmentRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void findAvailableAppointmentsBySpecialty_ShouldReturnPageOfAppointmentResponseDTO_WhenSuccessful() {
        Pageable pageable = PageRequest.of(0, 10);
        Appointment appointment = freshAppointmentEntity();
        Page<Appointment> page = new PageImpl<>(List.of(appointment), pageable, 1);

        when(appointmentRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);

        Page<AppointmentResponseDTO> responsePage = appointmentService.findAvailableAppointmentsBySpecialty(
                1L, pageable);

        assertThat(responsePage).isNotNull();
        assertThat(responsePage.getTotalElements()).isEqualTo(1);
        assertThat(responsePage.getContent().get(0).id()).isEqualTo(AppointmentConstants.APPOINTMENT_ID);

        verify(appointmentRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void createSlot_ShouldCreateAppointment_WhenDataIsValid() {
        var dto = AppointmentConstants.APPOINTMENT_CREATE_SLOT_DTO;

        Assistant assistant = freshAssistantEntity();
        DoctorSchedule schedule = freshDoctorScheduleEntity();

        when(assistantRepository.findById(ASSISTANT_ID)).thenReturn(Optional.of(assistant));
        when(doctorScheduleRepository.findById(dto.doctorScheduleId())).thenReturn(Optional.of(schedule));
        when(appointmentRepository.existsByDoctorScheduleIdAndDateTime(dto.doctorScheduleId(), dto.dateTime())).thenReturn(false);
        when(appointmentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        AppointmentResponseDTO response = appointmentService.createSlot(dto, ASSISTANT_ID);

        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(AppointmentStatus.AVAILABLE);
        verify(assistantRepository).findById(ASSISTANT_ID);
        verify(doctorScheduleRepository).findById(dto.doctorScheduleId());
        verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    void createSlot_ShouldThrowResourceNotFoundException_WhenAssistantNotFound() {
        var dto = AppointmentConstants.APPOINTMENT_CREATE_SLOT_DTO;

        when(assistantRepository.findById(ASSISTANT_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> appointmentService.createSlot(dto, ASSISTANT_ID));
        verify(assistantRepository).findById(ASSISTANT_ID);
    }

    @Test
    void createSlot_ShouldThrowResourceNotFoundException_WhenDoctorScheduleNotFound() {
        var dto = AppointmentConstants.APPOINTMENT_CREATE_SLOT_DTO;
        Assistant assistant = freshAssistantEntity();

        when(assistantRepository.findById(ASSISTANT_ID)).thenReturn(Optional.of(assistant));
        when(doctorScheduleRepository.findById(dto.doctorScheduleId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> appointmentService.createSlot(dto, ASSISTANT_ID));
        verify(assistantRepository).findById(ASSISTANT_ID);
        verify(doctorScheduleRepository).findById(dto.doctorScheduleId());
    }

    @Test
    void createSlot_ShouldThrowBadRequestException_WhenDayOfWeekDoesNotMatch() {
        var dto = AppointmentConstants.APPOINTMENT_CREATE_SLOT_DTO;
        Assistant assistant = freshAssistantEntity();

        DoctorSchedule schedule = freshDoctorScheduleEntity();
        schedule.setDayOfWeek(DayOfWeek.TUESDAY);

        when(assistantRepository.findById(ASSISTANT_ID)).thenReturn(Optional.of(assistant));
        when(doctorScheduleRepository.findById(dto.doctorScheduleId())).thenReturn(Optional.of(schedule));

        assertThrows(BadRequestException.class, () -> appointmentService.createSlot(dto, ASSISTANT_ID));
    }

    @Test
    void createSlot_ShouldThrowBadRequestException_WhenTimeIsOutsideSchedule() {
        var dto = AppointmentConstants.APPOINTMENT_CREATE_SLOT_DTO;
        Assistant assistant = freshAssistantEntity();

        DoctorSchedule schedule = freshDoctorScheduleEntity();

        schedule.setStartTime(dto.dateTime().toLocalTime().plusHours(1));
        schedule.setEndTime(dto.dateTime().toLocalTime().plusHours(2));

        when(assistantRepository.findById(ASSISTANT_ID)).thenReturn(Optional.of(assistant));
        when(doctorScheduleRepository.findById(dto.doctorScheduleId())).thenReturn(Optional.of(schedule));

        assertThrows(BadRequestException.class, () -> appointmentService.createSlot(dto, ASSISTANT_ID));
    }

    @Test
    void createSlot_ShouldThrowResourceAlreadyExistsException_WhenConflictExists() {
        var dto = AppointmentConstants.APPOINTMENT_CREATE_SLOT_DTO;
        Assistant assistant = freshAssistantEntity();
        DoctorSchedule schedule = freshDoctorScheduleEntity();

        when(assistantRepository.findById(ASSISTANT_ID)).thenReturn(Optional.of(assistant));
        when(doctorScheduleRepository.findById(dto.doctorScheduleId())).thenReturn(Optional.of(schedule));

        when(appointmentRepository.existsByDoctorScheduleIdAndDateTime(dto.doctorScheduleId(), dto.dateTime())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> appointmentService.createSlot(dto, ASSISTANT_ID));
    }

    @Test
    void updateStatus_ShouldUpdateSuccessfully_WhenValidTransition() {
        Appointment appointment = freshAppointmentEntity();
        appointment.setStatus(AppointmentStatus.BOOKED);

        when(appointmentRepository.findById(APPOINTMENT_ID)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        AppointmentResponseDTO response = appointmentService.updateStatus(APPOINTMENT_ID, AppointmentStatus.CANCELED);

        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(AppointmentStatus.CANCELED);
        verify(appointmentRepository).findById(APPOINTMENT_ID);
        verify(appointmentRepository).save(appointment);
    }

    @Test
    void updateStatus_ShouldThrowBadRequest_WhenInvalidTransition() {
        Appointment appointment = freshAppointmentEntity();
        appointment.setStatus(AppointmentStatus.CANCELED);

        when(appointmentRepository.findById(APPOINTMENT_ID)).thenReturn(Optional.of(appointment));

        assertThrows(BadRequestException.class, () ->
                appointmentService.updateStatus(APPOINTMENT_ID, AppointmentStatus.BOOKED));

        verify(appointmentRepository).findById(APPOINTMENT_ID);
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void updateStatus_ShouldThrowResourceNotFound_WhenAppointmentNotExists() {
        UUID nonexistentId = UUID.randomUUID();
        when(appointmentRepository.findById(nonexistentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> appointmentService.updateStatus(nonexistentId, AppointmentStatus.BOOKED));

        verify(appointmentRepository).findById(nonexistentId);
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void bookAppointment_ShouldBook_WhenSlotIsAvailableAndInFuture() {
        Appointment appointment = freshAppointmentEntity();
        appointment.setStatus(AppointmentStatus.AVAILABLE);
        appointment.setDateTime(LocalDateTime.now().plusDays(1));

        Patient patient = new Patient();
        patient.setId(PATIENT_ID);

        when(appointmentRepository.findById(APPOINTMENT_ID)).thenReturn(Optional.of(appointment));
        when(patientRepository.findById(PATIENT_ID)).thenReturn(Optional.of(patient));
        when(appointmentRepository.save(any())).thenReturn(appointment);

        AppointmentResponseDTO response = appointmentService.bookAppointment(
                APPOINTMENT_BOOK_DTO,
                PATIENT_ID);

        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(AppointmentStatus.BOOKED);

        verify(appointmentRepository).save(appointment);
        verify(appointmentProducer).publishAppointmentEvent(any());
    }

    @Test
    void bookAppointment_ShouldThrowBadRequest_WhenStatusIsNotAvailable() {
        Appointment appointment = freshAppointmentEntity();
        appointment.setStatus(AppointmentStatus.BOOKED);

        when(appointmentRepository.findById(APPOINTMENT_ID)).thenReturn(Optional.of(appointment));

        assertThrows(BadRequestException.class, () ->
                appointmentService.bookAppointment(APPOINTMENT_BOOK_DTO, PATIENT_ID));

        verify(appointmentRepository, never()).save(any());
        verify(appointmentProducer, never()).publishAppointmentEvent(any());
    }

    @Test
    void bookAppointment_ShouldThrowBadRequest_WhenDateTimeIsInPast() {
        Appointment appointment = freshAppointmentEntity();
        appointment.setStatus(AppointmentStatus.AVAILABLE);
        appointment.setDateTime(LocalDateTime.now().minusHours(1));

        when(appointmentRepository.findById(APPOINTMENT_ID)).thenReturn(Optional.of(appointment));

        assertThrows(BadRequestException.class, () ->
                appointmentService.bookAppointment(APPOINTMENT_BOOK_DTO, PATIENT_ID));

        verify(appointmentRepository, never()).save(any());
        verify(appointmentProducer, never()).publishAppointmentEvent(any());
    }

    @Test
    void bookAppointment_ShouldThrowResourceNotFound_WhenPatientNotFound() {
        Appointment appointment = freshAppointmentEntity();
        appointment.setStatus(AppointmentStatus.AVAILABLE);
        appointment.setDateTime(LocalDateTime.now().plusDays(1));

        when(appointmentRepository.findById(APPOINTMENT_ID)).thenReturn(Optional.of(appointment));
        when(patientRepository.findById(PATIENT_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                appointmentService.bookAppointment(APPOINTMENT_BOOK_DTO, PATIENT_ID));

        verify(appointmentRepository, never()).save(any());
        verify(appointmentProducer, never()).publishAppointmentEvent(any());
    }

    @Test
    void delete_ShouldSucceed_WhenStatusIsAvailable() {
        Appointment appointment = AppointmentConstants.freshAppointmentEntity();
        appointment.setStatus(AppointmentStatus.AVAILABLE);

        when(appointmentRepository.findById(APPOINTMENT_ID)).thenReturn(Optional.of(appointment));

        appointmentService.delete(APPOINTMENT_ID);

        verify(appointmentRepository).delete(appointment);
    }

    @Test
    void delete_ShouldSucceed_WhenStatusIsCanceled() {
        Appointment appointment = AppointmentConstants.freshAppointmentEntity();
        appointment.setStatus(AppointmentStatus.CANCELED);

        when(appointmentRepository.findById(APPOINTMENT_ID)).thenReturn(Optional.of(appointment));

        appointmentService.delete(APPOINTMENT_ID);

        verify(appointmentRepository).delete(appointment);
    }

    @Test
    void delete_ShouldThrowBadRequest_WhenStatusIsNotDeletable() {
        Appointment appointment = AppointmentConstants.freshAppointmentEntity();
        appointment.setStatus(AppointmentStatus.BOOKED);

        when(appointmentRepository.findById(APPOINTMENT_ID)).thenReturn(Optional.of(appointment));

        assertThatThrownBy(() -> appointmentService.delete(APPOINTMENT_ID))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Cannot delete an appointment that is not available or canceled");
    }

    @Test
    void delete_ShouldThrowResourceNotFound_WhenAppointmentDoesNotExist() {
        UUID invalidId = UUID.randomUUID();
        when(appointmentRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> appointmentService.delete(invalidId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Appointment not found with id");
    }
}
