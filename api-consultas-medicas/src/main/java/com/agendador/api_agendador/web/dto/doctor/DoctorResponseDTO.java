package com.agendador.api_agendador.web.dto.doctor;

import java.time.LocalDate;
import java.util.Set;

public record DoctorResponseDTO(
        Long id,
        String name,
        String crm,
        String email,
        String phone,
        LocalDate birthDate,
        String role,
        Set<Long> specialtyIds
) {}
