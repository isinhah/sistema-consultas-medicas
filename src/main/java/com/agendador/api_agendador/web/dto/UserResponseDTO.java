package com.agendador.api_agendador.web.dto;

import java.time.LocalDateTime;

public record UserResponseDTO(
        String name,
        String email,
        String password,
        String phone,
        String role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}