package com.agendador.api_agendador.web.mapper;

import com.agendador.api_agendador.entity.User;
import com.agendador.api_agendador.entity.enums.Role;
import com.agendador.api_agendador.web.dto.UserCreateDTO;
import com.agendador.api_agendador.web.dto.UserResponseDTO;
import com.agendador.api_agendador.web.dto.UserUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "role", target = "role", qualifiedByName = "mapStringToRole")
    User toEntity(UserCreateDTO dto);

    UserResponseDTO toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateDto(UserUpdateDTO dto, @MappingTarget User user);

    @Named("mapStringToRole")
    default Role mapStringToRole(String role) {
        return Role.fromString(role);
    }
}