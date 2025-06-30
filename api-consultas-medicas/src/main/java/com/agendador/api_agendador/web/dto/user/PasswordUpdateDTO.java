package com.agendador.api_agendador.web.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO for updating user password")
public record PasswordUpdateDTO(

        @Schema(
                description = "Current password of the user",
                example = "12345678"
        )
        @NotBlank(message = "Current password must not be blank")
        String currentPassword,

        @Schema(
                description = "New password to be set (exactly 8 characters)",
                example = "11111111"
        )
        @NotBlank(message = "New password must not be blank")
        @Size(min = 8, max = 8, message = "Password size must be exactly 8 characters")
        String newPassword
) {
}