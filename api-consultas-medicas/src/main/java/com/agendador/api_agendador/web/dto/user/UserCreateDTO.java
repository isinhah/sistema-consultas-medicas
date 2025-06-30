package com.agendador.api_agendador.web.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Schema(description = "DTO for user creation")
public record UserCreateDTO(

        @Schema(
                description = "Full name of the user",
                example = "John Doe"
        )
        @NotBlank(message = "Name cannot be empty")
        @Size(max = 150, message = "Name must be at most 150 characters long")
        String name,


        @Schema(
                description = "User's email address",
                example = "john.doe@example.com"
        )
        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Email format is invalid", regexp = "^[a-z0-9.+-_]+@[a-z0-9.-]+\\.[a-z]{2,}$")
        @Size(max = 100, message = "Email must be at most 100 characters long")
        String email,

        @Schema(
                description = "User's password (exactly 8 characters)",
                example = "12345678"
        )
        @NotBlank(message = "Password cannot be empty")
        @Size(min = 8, max = 8, message = "Password size must be exactly 8 characters")
        String password,

        @Schema(
                description = "User's phone number",
                example = "(11)91234-5678"
        )
        @NotBlank(message = "Phone cannot be empty")
        @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Phone number format is invalid")
        String phone,

        @Schema(
                description = "User's birth date (must be in the past)",
                example = "1990-05-20"
        )
        @NotNull(message = "Birth date cannot be null")
        @Past(message = "Birth date must be in the past")
        LocalDate birthDate
) {
}