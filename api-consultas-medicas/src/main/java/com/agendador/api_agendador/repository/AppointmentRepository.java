package com.agendador.api_agendador.repository;

import com.agendador.api_agendador.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID>, JpaSpecificationExecutor<Appointment> {
    // Verifica se já existe Appointment no mesmo horário para o mesmo médico
    boolean existsByDoctorScheduleIdAndDateTime(Long doctorScheduleId, LocalDateTime dateTime);

    // Verifica se já existe um slot (appointment) criado pelo assistente no mesmo horário
    boolean existsByAssistantIdAndDoctorScheduleIdAndDateTime(Long assistantId, Long doctorScheduleId, LocalDateTime dateTime);

    // Verifica conflito ao atualizar um slot (appointment)
    boolean existsByDoctorScheduleIdAndDateTimeAndIdNot(Long doctorScheduleId, LocalDateTime dateTime, UUID excludeId);
}