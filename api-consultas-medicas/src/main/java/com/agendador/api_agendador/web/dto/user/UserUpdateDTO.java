package com.agendador.api_agendador.web.dto.user;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UserUpdateDTO(
        @NotBlank(message = "Name cannot be empty")
        @Size(max = 150)
        String name,

        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Email format is invalid", regexp = "^[a-z0-9.+-_]+@[a-z0-9.-]+\\.[a-z]{2,}$")
        @Size(max = 100, message = "Email must be at most 100 characters long")
        String email,

        @NotBlank(message = "Phone cannot be empty")
        @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$")
        String phone,

        @NotNull(message = "Birth date cannot be null")
        @Past(message = "Birth date must be in the past")
        LocalDate birthDate
) {
}
