package com.agendador.api_agendador.messaging.dto;

import java.util.UUID;

public record AppointmentEvent(
        UUID id,
        String patientName,
        String doctorName,
        String dateTime
) {}