package com.agendador.api_agendador.web.dto;

import java.util.Set;

public record DoctorResponseDTO(
        Long id,
        String name,
        String crm,
        String email,
        String phone,
        String birthDate,
        String role,
        Set<Long> specialtyIds
) {}
