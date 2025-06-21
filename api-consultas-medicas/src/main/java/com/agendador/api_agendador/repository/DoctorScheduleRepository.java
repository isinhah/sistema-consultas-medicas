package com.agendador.api_agendador.repository;

import com.agendador.api_agendador.entity.DoctorSchedule;
import com.agendador.api_agendador.entity.enums.DayOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalTime;

public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long>, JpaSpecificationExecutor<DoctorSchedule> {
    // Verifica se um novo horário (na criação) conflita com algum existente do mesmo médico no mesmo dia
    boolean existsByDoctorIdAndDayOfWeekAndStartTimeLessThanAndEndTimeGreaterThan(
            Long doctorId, DayOfWeek dayOfWeek, LocalTime endTime, LocalTime startTime);

    // Verifica conflitos na atualização, ignorando o próprio horário que está sendo editado
    boolean existsByDoctorIdAndDayOfWeekAndStartTimeLessThanAndEndTimeGreaterThanAndIdNot(
            Long doctorId, DayOfWeek dayOfWeek, LocalTime endTime, LocalTime startTime, Long excludeId);
}