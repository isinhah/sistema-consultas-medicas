package com.agendador.api_agendador.service;

import com.agendador.api_agendador.entity.Appointment;
import com.agendador.api_agendador.entity.Doctor;
import com.agendador.api_agendador.entity.DoctorSchedule;
import com.agendador.api_agendador.repository.DoctorRepository;
import com.agendador.api_agendador.repository.DoctorScheduleRepository;
import com.agendador.api_agendador.util.DoctorScheduleConstants;
import com.agendador.api_agendador.web.dto.doctor_schedule.DoctorScheduleResponseDTO;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.agendador.api_agendador.util.DoctorScheduleConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DoctorScheduleServiceTest {

    @InjectMocks
    private DoctorScheduleService doctorScheduleService;

    @Mock
    private DoctorScheduleRepository doctorScheduleRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Test
    void findById_ShouldReturnSchedule_WhenFound() {
        DoctorSchedule schedule = freshDoctorScheduleEntity();

        when(doctorScheduleRepository.findById(SCHEDULE_ID)).thenReturn(Optional.of(schedule));

        var response = doctorScheduleService.findById(SCHEDULE_ID);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(SCHEDULE_ID);
        verify(doctorScheduleRepository).findById(SCHEDULE_ID);
    }

    @Test
    void findById_ShouldThrowResourceNotFound_WhenNotFound() {
        when(doctorScheduleRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> doctorScheduleService.findById(SCHEDULE_ID));
    }

    @Test
    void findSchedulesByDoctorId_ShouldReturnPageOfDoctorScheduleResponseDTO_WhenSuccess() {
        Pageable pageable = PageRequest.of(0, 10);
        DoctorSchedule schedule = DoctorScheduleConstants.freshDoctorScheduleEntity();
        Page<DoctorSchedule> page = new PageImpl<>(List.of(schedule), pageable, 1);

        when(doctorScheduleRepository.findAll(
                any(Specification.class),
                eq(pageable)))
                .thenReturn(page);

        Page<DoctorScheduleResponseDTO> responsePage = doctorScheduleService.findSchedulesByDoctorId(
                DOCTOR_ID,
                DAY_OF_WEEK_CREATE,
                START_TIME_CREATE,
                END_TIME_CREATE,
                pageable);

        assertThat(responsePage).isNotNull();
        assertThat(responsePage.getTotalElements()).isEqualTo(1);
        assertThat(responsePage.getContent().get(0).id()).isEqualTo(SCHEDULE_ID);

        verify(doctorScheduleRepository).findAll(
                any(Specification.class),
                eq(pageable)
        );
    }

    @Test
    void findAvailableSchedulesBySpecialty_ShouldReturnPageOfDoctorScheduleResponseDTO_WhenSuccess() {
        Pageable pageable = PageRequest.of(0, 10);
        DoctorSchedule schedule = DoctorScheduleConstants.freshDoctorScheduleEntity();
        Page<DoctorSchedule> page = new PageImpl<>(List.of(schedule), pageable, 1);

        when(doctorScheduleRepository.findAll(
                any(Specification.class),
                eq(pageable)))
                .thenReturn(page);

        Page<DoctorScheduleResponseDTO> responsePage = doctorScheduleService.findAvailableSchedulesBySpecialty(
                1L,
                pageable);

        assertThat(responsePage).isNotNull();
        assertThat(responsePage.getTotalElements()).isEqualTo(1);
        assertThat(responsePage.getContent().get(0).id()).isEqualTo(SCHEDULE_ID);

        verify(doctorScheduleRepository).findAll(
                any(Specification.class),
                eq(pageable)
        );
    }

    @Test
    void create_ShouldReturnDoctorScheduleResponse_WhenSuccess() {
        Doctor doctor = new Doctor();
        doctor.setId(DOCTOR_ID);

        when(doctorRepository.existsById(DOCTOR_ID)).thenReturn(true);
        when(doctorScheduleRepository.existsByDoctorIdAndDayOfWeekAndStartTimeLessThanAndEndTimeGreaterThan(
                DOCTOR_ID,
                DOCTOR_SCHEDULE_CREATE_DTO.dayOfWeek(),
                DOCTOR_SCHEDULE_CREATE_DTO.endTime(),
                DOCTOR_SCHEDULE_CREATE_DTO.startTime()))
                .thenReturn(false);
        when(doctorRepository.getReferenceById(DOCTOR_ID)).thenReturn(doctor);
        when(doctorScheduleRepository.save(any(DoctorSchedule.class))).thenAnswer(i -> i.getArgument(0));

        var response = doctorScheduleService.create(DOCTOR_SCHEDULE_CREATE_DTO);

        assertThat(response).isNotNull();
        assertThat(response.dayOfWeek()).isEqualTo(DOCTOR_SCHEDULE_CREATE_DTO.dayOfWeek());
        verify(doctorRepository).existsById(DOCTOR_ID);
        verify(doctorScheduleRepository).save(any(DoctorSchedule.class));
    }

    @Test
    void create_ShouldThrowResourceNotFound_WhenDoctorDoesNotExist() {
        when(doctorRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> doctorScheduleService.create(DOCTOR_SCHEDULE_CREATE_DTO));
    }

    @Test
    void create_ShouldThrowResourceAlreadyExists_WhenScheduleConflict() {
        when(doctorRepository.existsById(anyLong())).thenReturn(true);
        when(doctorScheduleRepository.existsByDoctorIdAndDayOfWeekAndStartTimeLessThanAndEndTimeGreaterThan(
                anyLong(), any(), any(), any()))
                .thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class,
                () -> doctorScheduleService.create(DOCTOR_SCHEDULE_CREATE_DTO));
    }

    @Test
    void update_ShouldReturnUpdatedSchedule_WhenSuccess() {
        DoctorSchedule existingSchedule = freshDoctorScheduleEntity();

        when(doctorScheduleRepository.findById(SCHEDULE_ID)).thenReturn(Optional.of(existingSchedule));
        when(doctorScheduleRepository.existsByDoctorIdAndDayOfWeekAndStartTimeLessThanAndEndTimeGreaterThanAndIdNot(
                existingSchedule.getDoctor().getId(),
                DOCTOR_SCHEDULE_UPDATE_DTO.dayOfWeek(),
                DOCTOR_SCHEDULE_UPDATE_DTO.endTime(),
                DOCTOR_SCHEDULE_UPDATE_DTO.startTime(),
                SCHEDULE_ID))
                .thenReturn(false);
        when(doctorScheduleRepository.save(any(DoctorSchedule.class))).thenAnswer(i -> i.getArgument(0));

        var response = doctorScheduleService.update(SCHEDULE_ID, DOCTOR_SCHEDULE_UPDATE_DTO);

        assertThat(response).isNotNull();
        assertThat(response.dayOfWeek()).isEqualTo(DOCTOR_SCHEDULE_UPDATE_DTO.dayOfWeek());
        verify(doctorScheduleRepository).save(existingSchedule);
    }

    @Test
    void update_ShouldThrowResourceNotFound_WhenScheduleNotFound() {
        when(doctorScheduleRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> doctorScheduleService.update(SCHEDULE_ID, DOCTOR_SCHEDULE_UPDATE_DTO));
    }

    @Test
    void update_ShouldThrowResourceAlreadyExists_WhenScheduleConflict() {
        DoctorSchedule existingSchedule = freshDoctorScheduleEntity();

        when(doctorScheduleRepository.findById(SCHEDULE_ID)).thenReturn(Optional.of(existingSchedule));
        when(doctorScheduleRepository.existsByDoctorIdAndDayOfWeekAndStartTimeLessThanAndEndTimeGreaterThanAndIdNot(
                anyLong(), any(), any(), any(), anyLong()))
                .thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> doctorScheduleService.update(SCHEDULE_ID, DOCTOR_SCHEDULE_UPDATE_DTO));
    }

    @Test
    void delete_ShouldDelete_WhenNoAppointments() {
        DoctorSchedule schedule = freshDoctorScheduleEntity();
        schedule.setAppointments(Collections.emptySet());

        when(doctorScheduleRepository.findById(SCHEDULE_ID)).thenReturn(Optional.of(schedule));
        doNothing().when(doctorScheduleRepository).delete(schedule);

        doctorScheduleService.delete(SCHEDULE_ID);

        verify(doctorScheduleRepository).delete(schedule);
    }

    @Test
    void delete_ShouldThrowBadRequest_WhenHasAppointments() {
        DoctorSchedule schedule = freshDoctorScheduleEntity();
        schedule.getAppointments().add(new Appointment());

        when(doctorScheduleRepository.findById(SCHEDULE_ID)).thenReturn(Optional.of(schedule));

        assertThrows(BadRequestException.class, () -> doctorScheduleService.delete(SCHEDULE_ID));
    }
}
