package com.agendador.api_agendador.web.dto.doctor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

@Schema(description = "DTO for doctor update")
public record DoctorUpdateDTO(

        @Schema(
                description = "Doctor's updated CRM",
                example = "MG432198"
        )
        @NotBlank(message = "CRM cannot be empty")
        String crm,

        @Schema(
                description = "Updated IDs of the doctor's specialties",
                example = "[1, 2]"
        )
        @NotEmpty(message = "At least one specialty must be selected")
        Set<Long> specialtyIds
) {}