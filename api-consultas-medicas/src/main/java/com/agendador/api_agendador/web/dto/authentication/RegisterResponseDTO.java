package com.agendador.api_agendador.web.dto.authentication;

import java.time.Instant;

public record RegisterResponseDTO(
        Long userId,
        String tokenType,
        String token,
        Instant expiresAt
) {}