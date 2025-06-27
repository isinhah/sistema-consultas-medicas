package com.isabel.api_comprovantes.messaging.dto;

import java.util.UUID;

public record AppointmentEvent(
        UUID id,
        String patientName,
        String doctorName,
        String dateTime
) {}