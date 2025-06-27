package com.agendador.api_agendador.web.dto.appointment;

import com.agendador.api_agendador.entity.enums.AppointmentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentResponseDTO(
        UUID id,
        String patientName,
        String doctorName,
        AppointmentStatus status,
        LocalDateTime dateTime
) {}