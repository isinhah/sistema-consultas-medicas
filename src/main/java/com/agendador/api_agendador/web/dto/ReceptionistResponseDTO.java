package com.agendador.api_agendador.web.dto;

import java.time.LocalDate;

public record ReceptionistResponseDTO(
        Long id,
        String name,
        String registrationNumber,
        String email,
        String phone,
        LocalDate birthDate,
        String role
) {
}