package com.agendador.api_agendador.web.mapper;

import com.agendador.api_agendador.entity.Patient;
import com.agendador.api_agendador.entity.enums.Role;
import com.agendador.api_agendador.web.dto.PatientCreateDTO;
import com.agendador.api_agendador.web.dto.PatientResponseDTO;
import com.agendador.api_agendador.web.dto.PatientUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PatientMapper {

    PatientMapper INSTANCE = Mappers.getMapper(PatientMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    Patient toEntity(PatientCreateDTO dto);

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "cpf", source = "cpf")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "phone", source = "user.phone")
    @Mapping(target = "birthDate", source = "user.birthDate")
    @Mapping(target = "role", source = "user.role", qualifiedByName = "roleToString")
    PatientResponseDTO toDto(Patient patient);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    void updateDto(PatientUpdateDTO dto, @MappingTarget Patient patient);

    @Named("roleToString")
    default String roleToString(Role role) {
        return role != null ? role.name() : null;
    }
}
