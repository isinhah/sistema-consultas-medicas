package com.agendador.api_agendador.repository;

import com.agendador.api_agendador.entity.Appointment;
import com.agendador.api_agendador.entity.enums.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    Page<Appointment> findByDoctorScheduleId(Long doctorScheduleId, Pageable pageable);
    Page<Appointment> findByStatus(AppointmentStatus status, Pageable pageable);
    Page<Appointment> findByAssistantId(Long assistantId, Pageable pageable);
    Page<Appointment> findByPatientId(Long patientId, Pageable pageable);

    Page<Appointment> findByDateTimeBetween(LocalDateTime startOfDay, LocalDateTime endOfDay, Pageable pageable);
    Page<Appointment> findByDateTimeAfter(LocalDateTime now, Pageable pageable);
    Page<Appointment> findByDateTimeBefore(LocalDateTime now, Pageable pageable);
}