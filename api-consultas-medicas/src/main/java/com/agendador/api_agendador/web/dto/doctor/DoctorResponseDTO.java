package com.agendador.api_agendador.web.dto.doctor;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.Set;

@Schema(description = "DTO for doctor response")
public record DoctorResponseDTO(

        @Schema(description = "Doctor ID", example = "2")
        Long id,

        @Schema(description = "Doctor's full name", example = "Dr. Mariana Costa")
        String name,

        @Schema(description = "Doctor's CRM", example = "RJ654321")
        String crm,

        @Schema(description = "Doctor's email address", example = "mariana.costa@clinic.com")
        String email,

        @Schema(description = "Doctor's phone number", example = "(22)91234-5678")
        String phone,

        @Schema(description = "Doctor's birth date", example = "1982-11-03")
        LocalDate birthDate,

        @Schema(description = "User role", example = "DOCTOR")
        String role,

        @Schema(description = "IDs of the doctor's specialties", example = "[1, 2]")
        Set<Long> specialtyIds
) {}