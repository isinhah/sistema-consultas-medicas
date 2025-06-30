package com.agendador.api_agendador.web.dto.appointment;

import com.agendador.api_agendador.entity.enums.AppointmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO to update appointment status")
public record AppointmentUpdateStatusDTO(

        @Schema(example = "CANCELED")
        @NotNull(message = "Status is required")
        AppointmentStatus status
) {}