package com.agendador.api_agendador.web.dto.appointment;

import com.agendador.api_agendador.entity.enums.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AppointmentResponseDTO(
        UUID id,
        String patientName,
        String doctorName,
        AppointmentStatus status,
        LocalDateTime dateTime
) {}