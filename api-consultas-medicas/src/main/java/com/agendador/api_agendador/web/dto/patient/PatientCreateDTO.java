package com.agendador.api_agendador.web.dto.patient;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PatientCreateDTO(
        @NotBlank(message = "CPF must be provided")
        @Pattern(regexp = "\\d{11}", message = "CPF must contain exactly 11 digits")
        String cpf
) {
}