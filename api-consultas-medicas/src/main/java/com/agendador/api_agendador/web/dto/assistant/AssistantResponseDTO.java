package com.agendador.api_agendador.web.dto.assistant;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "DTO for assistant response")
public record AssistantResponseDTO(

        @Schema(description = "Unique identifier of the assistant", example = "3")
        Long id,

        @Schema(description = "Assistant's full name", example = "Ana Maria")
        String name,

        @Schema(description = "Assistant's registration number", example = "REC20240002")
        String registrationNumber,

        @Schema(description = "Assistant's email address", example = "ana.maria@clinic.com")
        String email,

        @Schema(description = "Assistant's phone number", example = "(21)98877-1122")
        String phone,

        @Schema(description = "Assistant's birth date", example = "1994-11-03")
        LocalDate birthDate,

        @Schema(description = "User role", example = "ASSISTANT")
        String role
) {
}