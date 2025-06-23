package com.agendador.api_agendador.web.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordUpdateDTO(
        @NotBlank(message = "Current password must not be blank")
        String currentPassword,

        @NotBlank(message = "New password must not be blank")
        @Size(min = 8, max = 8, message = "Password size must be exactly 8 characters")
        String newPassword
) {}