package com.agendador.api_agendador.web.dto.doctor_schedule;

import com.agendador.api_agendador.entity.enums.DayOfWeek;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

@Schema(description = "Data to update a doctor's schedule")
public record DoctorScheduleUpdateDTO(

        @Schema(
                description = "Day of the week",
                example = "TUESDAY"
        )
        @NotNull(message = "Day of week must be provided")
        DayOfWeek dayOfWeek,

        @Schema(
                description = "Start time",
                example = "14:00"
        )
        @NotNull(message = "Start time must be provided")
        LocalTime startTime,

        @Schema(
                description = "End time",
                example = "18:00"
        )
        @NotNull(message = "End time must be provided")
        LocalTime endTime
) {}