package com.agendador.api_agendador.web.dto.patient;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "DTO for creating a patient")
public record PatientCreateDTO(

        @Schema(
                description = "CPF with exactly 11 digits",
                example = "12345678901"
        )
        @NotBlank(message = "CPF must be provided")
        @Pattern(regexp = "\\d{11}", message = "CPF must contain exactly 11 digits")
        String cpf
) {}