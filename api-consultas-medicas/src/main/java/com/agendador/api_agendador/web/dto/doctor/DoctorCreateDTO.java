package com.agendador.api_agendador.web.dto.doctor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Set;

public record DoctorCreateDTO(
        @NotNull(message = "User ID cannot be null")
        Long userId,

        @NotBlank(message = "CRM cannot be empty")
        @Pattern(regexp = "^[A-Z]{2,3}\\d{4,6}$", message = "CRM must follow the format: UF + 4 to 6 digits, e.g., SP123456")
        String crm,

        @NotEmpty(message = "At least one specialty must be selected")
        Set<Long> specialtyIds
) {
}