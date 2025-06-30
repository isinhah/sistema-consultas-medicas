package com.agendador.api_agendador.web.dto.specialty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO for specialty response")
public record SpecialtyResponseDTO(

        @Schema(description = "Unique identifier of the specialty", example = "1")
        Long id,

        @Schema(description = "Name of the specialty", example = "Cardiology")
        String name
) {
}