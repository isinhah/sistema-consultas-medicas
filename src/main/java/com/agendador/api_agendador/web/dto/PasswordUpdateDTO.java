package com.agendador.api_agendador.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordUpdateDTO(
        @NotBlank(message = "Current password cannot be empty")
        String currentPassword,

        @NotBlank(message = "New password cannot be empty")
        @Size(min = 8, max = 8, message = "Password size must be exactly 8 characters")
        String newPassword
) {}

