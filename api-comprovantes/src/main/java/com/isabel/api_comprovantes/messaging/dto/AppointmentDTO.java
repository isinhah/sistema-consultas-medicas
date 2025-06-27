package com.isabel.api_comprovantes.messaging.dto;

import java.util.UUID;

public record AppointmentDTO(
        UUID id,
        String patientName,
        String doctorName,
        String date,
        String time
) {}