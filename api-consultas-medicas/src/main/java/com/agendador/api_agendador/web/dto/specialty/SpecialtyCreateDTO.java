package com.agendador.api_agendador.web.dto.specialty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SpecialtyCreateDTO(
        @NotBlank(message = "Name must be provided")
        @Size(max = 100, message = "Name must be at most 100 characters long")
        String name
) {
}
