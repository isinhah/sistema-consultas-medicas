package com.agendador.api_agendador.web.dto.doctor_schedule;

import com.agendador.api_agendador.entity.enums.DayOfWeek;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record DoctorScheduleCreateDTO(
        @NotNull(message = "Doctor ID must be provided")
        Long doctorId,

        @NotNull(message = "Day of week must be provided")
        DayOfWeek dayOfWeek,

        @NotNull(message = "Start time must be provided")
        LocalTime startTime,

        @NotNull(message = "End time must be provided")
        LocalTime endTime
) {}