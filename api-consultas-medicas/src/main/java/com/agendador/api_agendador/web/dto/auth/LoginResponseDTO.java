package com.agendador.api_agendador.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "DTO for login response")
public record LoginResponseDTO(

        @Schema(description = "User identifier", example = "1")
        Long userId,

        @Schema(description = "Token type", example = "Bearer")
        String tokenType,

        @Schema(description = "JWT token string")
        String token,

        @Schema(description = "User role", example = "ADMIN")
        String role,

        @Schema(description = "Token expiration timestamp")
        Instant expiresAt
) {
}