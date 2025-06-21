package com.agendador.api_agendador.web.dto.doctor;

import com.agendador.api_agendador.entity.Doctor;
import com.agendador.api_agendador.entity.Specialty;
import com.agendador.api_agendador.entity.enums.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface DoctorMapper {

    DoctorMapper INSTANCE = Mappers.getMapper(DoctorMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "specialties", ignore = true)
    @Mapping(target = "schedules", ignore = true)
    Doctor toEntity(DoctorCreateDTO dto);

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "crm", source = "crm")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "phone", source = "user.phone")
    @Mapping(target = "birthDate", source = "user.birthDate")
    @Mapping(target = "role", source = "user.role", qualifiedByName = "roleToString")
    @Mapping(target = "specialtyIds", source = "specialties", qualifiedByName = "specialtiesToIds")
    DoctorResponseDTO toDto(Doctor doctor);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "specialties", ignore = true)
    @Mapping(target = "schedules", ignore = true)
    void updateDto(DoctorUpdateDTO dto, @MappingTarget Doctor doctor);

    @Named("roleToString")
    default String roleToString(Role role) {
        return role != null ? role.name() : null;
    }

    @Named("specialtiesToIds")
    default Set<Long> specialtiesToIds(Set<Specialty> specialties) {
        if (specialties == null) {
            return Set.of();
        }
        return specialties.stream()
                .map(Specialty::getId)
                .collect(Collectors.toSet());
    }
}