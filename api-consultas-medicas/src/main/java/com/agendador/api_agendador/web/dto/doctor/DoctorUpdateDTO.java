package com.agendador.api_agendador.web.dto.doctor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record DoctorUpdateDTO(
        @NotBlank(message = "CRM cannot be empty")
        String crm,

        @NotEmpty(message = "At least one specialty must be selected")
        Set<Long> specialtyIds
) {
}
