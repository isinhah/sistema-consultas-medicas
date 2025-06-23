package com.agendador.api_agendador.web.dto.appointment;

import com.agendador.api_agendador.entity.enums.AppointmentStatus;
import jakarta.validation.constraints.NotNull;

public record AppointmentUpdateStatusDTO(
        @NotNull(message = "Status is required")
        AppointmentStatus status
) {}