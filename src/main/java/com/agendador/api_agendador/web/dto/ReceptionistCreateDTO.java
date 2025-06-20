package com.agendador.api_agendador.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ReceptionistCreateDTO(
        @NotNull(message = "User ID cannot be null")
        Long userId,

        @NotBlank(message = "Registration number must not be blank")
        @Pattern(regexp = "^REC-?\\d{4}-?\\d{4}$",
                message = "Registration number must follow the pattern REC20250001 or REC-2025-0001")
        String registrationNumber
) {
}