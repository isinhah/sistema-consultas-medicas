package com.agendador.api_agendador.web.dto.assistant;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

@Schema(description = "DTO for assistant update")
public record AssistantUpdateDTO(

        @Schema(
                description = "Updated registration number",
                example = "REC-2025-0002"
        )
        @Pattern(
                regexp = "^REC-?\\d{4}-?\\d{4}$",
                message = "Registration number must follow the pattern REC20250001 or REC-2025-0001"
        )
        String registrationNumber
) {
}