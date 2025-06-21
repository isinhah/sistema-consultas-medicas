package com.agendador.api_agendador.web.dto.specialty;

import com.agendador.api_agendador.entity.Specialty;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SpecialtyMapper {

    SpecialtyMapper INSTANCE = Mappers.getMapper(SpecialtyMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctors", ignore = true)
    Specialty toEntity(SpecialtyCreateDTO dto);

    SpecialtyResponseDTO toDto(Specialty specialty);
}