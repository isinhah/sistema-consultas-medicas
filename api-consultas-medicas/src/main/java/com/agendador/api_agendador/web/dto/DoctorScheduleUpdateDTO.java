package com.agendador.api_agendador.web.dto;

import com.agendador.api_agendador.entity.enums.DayOfWeek;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record DoctorScheduleUpdateDTO(
        @NotNull(message = "Day of week must be provided")
        DayOfWeek dayOfWeek,

        @NotNull(message = "Start time must be provided")
        LocalTime startTime,

        @NotNull(message = "End time must be provided")
        LocalTime endTime
) {}