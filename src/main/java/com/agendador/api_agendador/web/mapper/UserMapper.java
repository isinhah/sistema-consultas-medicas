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
        @Mapping(target = "role", ignore = true)
        @Mapping(target = "patient", ignore = true)
        @Mapping(target = "doctor", ignore = true)
        @Mapping(target = "receptionist", ignore = true)
        User toEntity(UserCreateDTO dto);

        @Mapping(source = "role", target = "role", qualifiedByName = "roleToString")
        UserResponseDTO toDto(User user);

        @Mapping(target = "id", ignore = true)
        @Mapping(target = "password", ignore = true)
        @Mapping(target = "role", ignore = true)
        @Mapping(target = "createdAt", ignore = true)
        @Mapping(target = "updatedAt", ignore = true)
        @Mapping(target = "patient", ignore = true)
        @Mapping(target = "doctor", ignore = true)
        @Mapping(target = "receptionist", ignore = true)
        void updateDto(UserUpdateDTO dto, @MappingTarget User user);

        @Named("roleToString")
        default String roleToString(Role role) {
            return role != null ? role.name() : null;
        }
    }