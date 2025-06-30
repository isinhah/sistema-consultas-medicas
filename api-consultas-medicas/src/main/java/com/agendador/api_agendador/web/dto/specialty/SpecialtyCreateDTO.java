package com.agendador.api_agendador.web.dto.specialty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO for creating a specialty")
public record SpecialtyCreateDTO(

        @Schema(description = "Name of the specialty", example = "Cardiology")
        @NotBlank(message = "Name must be provided")
        @Size(max = 100, message = "Name must be at most 100 characters long")
        String name
) {
}