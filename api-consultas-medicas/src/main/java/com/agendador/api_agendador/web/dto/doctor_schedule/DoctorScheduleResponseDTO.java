package com.agendador.api_agendador.web.dto.doctor_schedule;

import com.agendador.api_agendador.entity.enums.DayOfWeek;

import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

public record DoctorScheduleResponseDTO(
        Long id,
        String doctorName,
        DayOfWeek dayOfWeek,
        LocalTime startTime,
        LocalTime endTime,
        Set<UUID> appointmentIds
) {}