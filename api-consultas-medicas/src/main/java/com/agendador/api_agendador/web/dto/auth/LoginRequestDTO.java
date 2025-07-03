package com.agendador.api_agendador.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO for login request")
public record LoginRequestDTO(

        @Schema(
                description = "User's email address",
                example = "john.doe@example.com"
        )
        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Email format is invalid", regexp = "^[a-z0-9.+-_]+@[a-z0-9.-]+\\.[a-z]{2,}$")
        @Size(max = 100, message = "Email must be at most 100 characters long")
        String email,

        @Schema(description = "User's password (exactly 8 characters)", example = "12345678")
        @NotBlank(message = "Password cannot be empty")
        @Size(min = 8, max = 8, message = "Password size must be exactly 8 characters")
        String password
) {
}