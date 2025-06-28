package com.agendador.api_agendador.web.dto.authentication;

import java.time.Instant;

public record LoginResponseDTO(
        Long userId,
        String tokenType,
        String token,
        String role,
        Instant expiresAt
) {}