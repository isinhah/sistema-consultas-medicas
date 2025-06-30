package com.agendador.api_agendador.web.dto.doctor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.util.Set;

@Schema(description = "DTO for doctor creation")
public record DoctorCreateDTO(

        @Schema(
                description = "Doctor's CRM (UF followed by 4 to 6 digits)",
                example = "SP123456"
        )
        @NotBlank(message = "CRM cannot be empty")
        @Pattern(regexp = "^[A-Z]{2,3}\\d{4,6}$", message = "CRM must follow the format: UF + 4 to 6 digits, e.g., SP123456")
        String crm,

        @Schema(
                description = "IDs of the doctor's specialties",
                example = "[1]"
        )
        @NotEmpty(message = "At least one specialty must be selected")
        Set<Long> specialtyIds
) {}