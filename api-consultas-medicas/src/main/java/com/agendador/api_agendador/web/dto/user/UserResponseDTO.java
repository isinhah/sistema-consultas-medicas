package com.agendador.api_agendador.web.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "DTO representing the user details returned in responses")
public record UserResponseDTO(

        @Schema(description = "Unique identifier of the user", example = "1")
        Long id,

        @Schema(description = "Full name of the user", example = "John Doe")
        String name,

        @Schema(description = "User's email address", example = "john.doe@example.com")
        String email,

        @Schema(description = "User's phone number", example = "(11)91234-5678")
        String phone,

        @Schema(description = "User's birth date", example = "1990-05-20")
        LocalDate birthDate,

        @Schema(description = "User's role in the system", example = "ADMIN")
        String role,

        @Schema(description = "Timestamp when the user was created", example = "20/07/2023 14:55:33")
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime createdAt,

        @Schema(description = "Timestamp when the user was last updated", example = "21/07/2023 09:12:05")
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime updatedAt
) {
}