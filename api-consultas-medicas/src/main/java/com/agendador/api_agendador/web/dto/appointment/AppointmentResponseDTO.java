package com.agendador.api_agendador.web.dto.appointment;

import com.agendador.api_agendador.entity.enums.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "DTO to represent an appointment")
public record AppointmentResponseDTO(

        @Schema(example = "c1a8c21f-3b9e-4fcb-b2a7-643fd7b11d2b")
        UUID id,

        @Schema(example = "John Doe")
        String patientName,

        @Schema(example = "Dr. Mariana Costa")
        String doctorName,

        @Schema(example = "BOOKED")
        AppointmentStatus status,

        @Schema(example = "2025-08-15T14:00:00")
        LocalDateTime dateTime
) {}