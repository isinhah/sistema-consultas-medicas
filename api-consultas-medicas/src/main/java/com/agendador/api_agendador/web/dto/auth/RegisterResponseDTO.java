package com.agendador.api_agendador.web.dto.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "DTO for register response")
public record RegisterResponseDTO(

        @Schema(description = "User identifier", example = "1")
        Long userId,

        @Schema(description = "Token type", example = "Bearer")
        String tokenType,

        @Schema(description = "JWT token string")
        String token,

        @Schema(description = "Token expiration timestamp")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
        Instant expiresAt
) {
}