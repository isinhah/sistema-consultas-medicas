package com.agendador.api_agendador.web.dto;

public record PatientResponseDTO(
        Long id,
        String name,
        String cpf,
        String email,
        String phone,
        String birthDate,
        String role
) {
}
