package com.agendador.api_agendador.web.mapper;

import com.agendador.api_agendador.entity.Specialty;
import com.agendador.api_agendador.web.dto.SpecialtyCreateDTO;
import com.agendador.api_agendador.web.dto.SpecialtyResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SpecialtyMapper {

    SpecialtyMapper INSTANCE = Mappers.getMapper(SpecialtyMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctors", ignore = true)
    Specialty toEntity(SpecialtyCreateDTO dto);

    SpecialtyResponseDTO toDto(Specialty specialty);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctors", ignore = true)
    void updateDto(SpecialtyCreateDTO dto, @MappingTarget Specialty specialty);
}