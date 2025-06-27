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
import com.agendador.api_agendador.specification.AppointmentSpecification;
import com.agendador.api_agendador.web.dto.appointment.AppointmentBookDTO;
import com.agendador.api_agendador.web.dto.appointment.AppointmentCreateSlotDTO;
import com.agendador.api_agendador.web.dto.appointment.AppointmentMapper;
import com.agendador.api_agendador.web.dto.appointment.AppointmentResponseDTO;
import com.agendador.api_agendador.web.exception.BadRequestException;
import com.agendador.api_agendador.web.exception.ResourceAlreadyExistsException;
import com.agendador.api_agendador.web.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorScheduleRepository doctorScheduleRepository;
    private final AssistantRepository assistantRepository;
    private final PatientRepository patientRepository;
    private final AppointmentProducer appointmentProducer;

    public AppointmentService(AppointmentRepository appointmentRepository, DoctorScheduleRepository doctorScheduleRepository, AssistantRepository assistantRepository, PatientRepository patientRepository, AppointmentProducer appointmentProducer) {
        this.appointmentRepository = appointmentRepository;
        this.doctorScheduleRepository = doctorScheduleRepository;
        this.assistantRepository = assistantRepository;
        this.patientRepository = patientRepository;
        this.appointmentProducer = appointmentProducer;
    }

    @Transactional(readOnly = true)
    public AppointmentResponseDTO findById(UUID id) {
        Appointment appointment = findEntityById(id);
        return AppointmentMapper.INSTANCE.toDto(appointment);
    }

    @Transactional(readOnly = true)
    public Page<AppointmentResponseDTO> findAppointmentsByFilters(Long doctorId,
                                                                  Long assistantId,
                                                                  Long patientId,
                                                                  AppointmentStatus status,
                                                                  LocalDateTime startDateTime,
                                                                  LocalDateTime endDateTime,
                                                                  Pageable pageable) {
        Specification<Appointment> spec = AppointmentSpecification.byFilters(doctorId, assistantId, patientId, status, startDateTime, endDateTime);
        Page<Appointment> page = appointmentRepository.findAll(spec, pageable);
        return page.map(AppointmentMapper.INSTANCE::toDto);
    }

    @Transactional(readOnly = true)
    public Page<AppointmentResponseDTO> findAvailableAppointmentsBySpecialty(Long specialtyId, Pageable pageable) {
        Specification<Appointment> spec = AppointmentSpecification.availableAppointmentsBySpecialty(specialtyId);
        Page<Appointment> page = appointmentRepository.findAll(spec, pageable);
        return page.map(AppointmentMapper.INSTANCE::toDto);
    }

    @Transactional
    public AppointmentResponseDTO createSlot(AppointmentCreateSlotDTO dto, Long assistantId) {
        Assistant assistant = assistantRepository.findById(assistantId)
                .orElseThrow(() -> new ResourceNotFoundException("Assistant not found"));

        DoctorSchedule schedule = doctorScheduleRepository.findById(dto.doctorScheduleId())
                .orElseThrow(() -> new ResourceNotFoundException("DoctorSchedule not found"));

        DayOfWeek expectedDay = DayOfWeek.valueOf(dto.dateTime().getDayOfWeek().name());
        if (!schedule.getDayOfWeek().equals(expectedDay)) {
            throw new BadRequestException("DateTime day must match DoctorSchedule day");
        }

        LocalTime time = dto.dateTime().toLocalTime();
        if (time.isBefore(schedule.getStartTime()) || time.isAfter(schedule.getEndTime())) {
            throw new BadRequestException("Time must be within DoctorSchedule working hours");
        }

        validateAppointmentConflict(dto.doctorScheduleId(), dto.dateTime(), null);

        Appointment appointment = AppointmentMapper.INSTANCE.toEntity(dto);
        appointment.setAssistant(assistant);
        appointment.setStatus(AppointmentStatus.AVAILABLE);
        appointment.setDateTime(dto.dateTime());

        appointmentRepository.save(appointment);

        return AppointmentMapper.INSTANCE.toDto(appointment);
    }

    @Transactional
    public AppointmentResponseDTO updateStatus(UUID appointmentId, AppointmentStatus newStatus) {
        Appointment appointment = findEntityById(appointmentId);
        AppointmentStatus currentStatus = appointment.getStatus();

        if (!isValidStatusTransition(currentStatus, newStatus)) {
            throw new BadRequestException(String.format("Invalid status transition: %s -> %s", currentStatus, newStatus));
        }

        appointment.setStatus(newStatus);
        appointmentRepository.save(appointment);

        return AppointmentMapper.INSTANCE.toDto(appointment);
    }

    @Transactional
    public AppointmentResponseDTO bookAppointment(AppointmentBookDTO dto, Long patientId) {
        Appointment appointment = findEntityById(dto.appointmentId());

        if (appointment.getStatus() != AppointmentStatus.AVAILABLE) {
            throw new BadRequestException("This appointment slot is not available for booking");
        }
        if (appointment.getDateTime().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Cannot book an appointment in the past");
        }

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));

        appointment.setPatient(patient);
        appointment.setStatus(AppointmentStatus.BOOKED);

        appointmentRepository.save(appointment);

        AppointmentResponseDTO response = AppointmentMapper.INSTANCE.toDto(appointment);
        appointmentProducer.publishAppointmentEvent(response);

        return response;
    }

    @Transactional
    public void delete(UUID appointmentId) {
        Appointment appointment = findEntityById(appointmentId);

        if (appointment.getStatus() != AppointmentStatus.AVAILABLE &&
                appointment.getStatus() != AppointmentStatus.CANCELED) {
            throw new BadRequestException("Cannot delete an appointment that is not available or canceled");
        }

        appointmentRepository.delete(appointment);
    }

    private boolean isValidStatusTransition(AppointmentStatus current, AppointmentStatus target) {
        return switch (current) {
            case BOOKED -> target == AppointmentStatus.CHECKED_IN
                    || target == AppointmentStatus.CANCELED
                    || target == AppointmentStatus.ABSENT;
            case CHECKED_IN -> target == AppointmentStatus.COMPLETED;
            case AVAILABLE -> target == AppointmentStatus.BOOKED;
            case COMPLETED, CANCELED, ABSENT -> false;
        };
    }

    private void validateAppointmentConflict(Long doctorScheduleId, LocalDateTime dateTime, UUID excludeId) {
        boolean exists;

        if (excludeId == null) {
            exists = appointmentRepository.existsByDoctorScheduleIdAndDateTime(doctorScheduleId, dateTime);
        } else {
            exists = appointmentRepository.existsByDoctorScheduleIdAndDateTimeAndIdNot(doctorScheduleId, dateTime, excludeId);
        }

        if (exists) {
            throw new ResourceAlreadyExistsException("An appointment already exists for this doctor schedule at this date and time.");
        }
    }

    @Transactional(readOnly = true)
    public Appointment findEntityById(UUID id) {
        return appointmentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Appointment not found with id: " + id)
        );
    }
}