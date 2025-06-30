package com.agendador.api_agendador.web.dto.appointment;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "DTO to book an appointment")
public record AppointmentBookDTO(

        @Schema(example = "c1a8c21f-3b9e-4fcb-b2a7-643fd7b11d2b")
        UUID appointmentId
) {}