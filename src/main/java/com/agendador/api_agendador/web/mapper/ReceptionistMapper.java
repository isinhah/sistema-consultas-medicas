package com.agendador.api_agendador.web.mapper;

import com.agendador.api_agendador.entity.Receptionist;
import com.agendador.api_agendador.entity.enums.Role;
import com.agendador.api_agendador.web.dto.ReceptionistCreateDTO;
import com.agendador.api_agendador.web.dto.ReceptionistResponseDTO;
import com.agendador.api_agendador.web.dto.ReceptionistUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReceptionistMapper {

    ReceptionistMapper INSTANCE = Mappers.getMapper(ReceptionistMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "confirmedAppointments", ignore = true)
    Receptionist toEntity(ReceptionistCreateDTO dto);

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "registrationNumber", source = "registrationNumber")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "phone", source = "user.phone")
    @Mapping(target = "birthDate", source = "user.birthDate")
    @Mapping(target = "role", source = "user.role", qualifiedByName = "roleToString")
    ReceptionistResponseDTO toDto(Receptionist receptionist);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "confirmedAppointments", ignore = true)
    void updateDto(ReceptionistUpdateDTO dto, @MappingTarget Receptionist receptionist);

    @Named("roleToString")
    default String roleToString(Role role) {
        return role != null ? role.name() : null;
    }
}