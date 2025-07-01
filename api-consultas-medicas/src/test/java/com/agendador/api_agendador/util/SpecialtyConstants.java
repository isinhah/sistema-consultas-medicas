package com.agendador.api_agendador.util;

import com.agendador.api_agendador.entity.Specialty;
import com.agendador.api_agendador.web.dto.specialty.SpecialtyCreateDTO;
import com.agendador.api_agendador.web.dto.specialty.SpecialtyResponseDTO;

public final class SpecialtyConstants {

    public static final Specialty SPECIALTY_CARDIOLOGY = createSpecialty(1L, "Cardiology");

    public static final SpecialtyCreateDTO SPECIALTY_CREATE_DTO = new SpecialtyCreateDTO("Cardiology");

    public static final SpecialtyResponseDTO SPECIALTY_RESPONSE_DTO_CARDIOLOGY = new SpecialtyResponseDTO(1L, "Cardiology");

    public static Specialty createSpecialty(Long id, String name) {
        Specialty specialty = new Specialty();
        specialty.setId(id);
        specialty.setName(name);
        return specialty;
    }
}
