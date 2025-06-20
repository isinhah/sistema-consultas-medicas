package com.agendador.api_agendador.web.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        String password,
        String phone,
        LocalDate birthDate,
        String role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}