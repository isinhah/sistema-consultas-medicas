package com.agendador.api_agendador.web.dto;

import com.agendador.api_agendador.entity.enums.DayOfWeek;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

public record DoctorScheduleResponseDTO(
        Long id,
        Long doctorId,
        DayOfWeek dayOfWeek,
        LocalTime startTime,
        LocalTime endTime,
        Set<UUID> appointmentIds
) {}