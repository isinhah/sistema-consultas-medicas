package com.agendador.api_agendador.web.mapper;

import com.agendador.api_agendador.entity.Assistant;
import com.agendador.api_agendador.entity.enums.Role;
import com.agendador.api_agendador.web.dto.AssistantCreateDTO;
import com.agendador.api_agendador.web.dto.AssistantResponseDTO;
import com.agendador.api_agendador.web.dto.AssistantUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AssistantMapper {

    AssistantMapper INSTANCE = Mappers.getMapper(AssistantMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "confirmedAppointments", ignore = true)
    Assistant toEntity(AssistantCreateDTO dto);

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "registrationNumber", source = "registrationNumber")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "phone", source = "user.phone")
    @Mapping(target = "birthDate", source = "user.birthDate")
    @Mapping(target = "role", source = "user.role", qualifiedByName = "roleToString")
    AssistantResponseDTO toDto(Assistant assistant);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "confirmedAppointments", ignore = true)
    void updateDto(AssistantUpdateDTO dto, @MappingTarget Assistant assistant);

    @Named("roleToString")
    default String roleToString(Role role) {
        return role != null ? role.name() : null;
    }
}