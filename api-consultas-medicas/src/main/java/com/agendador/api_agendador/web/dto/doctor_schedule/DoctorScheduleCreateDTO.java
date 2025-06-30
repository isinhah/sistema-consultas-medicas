package com.agendador.api_agendador.web.dto.doctor_schedule;

import com.agendador.api_agendador.entity.enums.DayOfWeek;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

@Schema(description = "Data to create a doctor's schedule")
public record DoctorScheduleCreateDTO(

        @Schema(description = "Doctor ID", example = "2")
        @NotNull(message = "Doctor ID must be provided")
        Long doctorId,

        @Schema(description = "Day of the week", example = "MONDAY")
        @NotNull(message = "Day of week must be provided")
        DayOfWeek dayOfWeek,

        @Schema(description = "Start time", example = "09:00")
        @NotNull(message = "Start time must be provided")
        LocalTime startTime,

        @Schema(description = "End time", example = "12:00")
        @NotNull(message = "End time must be provided")
        LocalTime endTime
) {}