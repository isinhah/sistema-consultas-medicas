package com.agendador.api_agendador.web.dto.appointment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Schema(description = "DTO to create an available slot")
public record AppointmentCreateSlotDTO(

        @Schema(
                description = "ID of the doctor's schedule",
                example = "1"
        )
        @NotNull(message = "DoctorSchedule ID must not be null")
        Long doctorScheduleId,

        @Schema(
                description = "Date and time of the appointment",
                example = "2025-08-15T14:00:00"
        )
        @NotNull(message = "Date and time must not be null")
        @Future(message = "Date and time must be in the future")
        LocalDateTime dateTime
) {}