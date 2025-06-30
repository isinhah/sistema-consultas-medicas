package com.agendador.api_agendador.web.dto.patient;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "DTO for patient response")
public record PatientResponseDTO(
        @Schema(description = "Patient ID", example = "1")
        Long id,

        @Schema(description = "Patient full name", example = "John Doe")
        String name,

        @Schema(description = "Patient CPF", example = "12345678901")
        String cpf,

        @Schema(description = "Patient email address", example = "john.doe@example.com")
        String email,

        @Schema(description = "Patient phone number", example = "+5511912345678")
        String phone,

        @Schema(description = "Patient birth date", example = "1990-05-20")
        LocalDate birthDate,

        @Schema(description = "Patient role", example = "PATIENT")
        String role
) {}