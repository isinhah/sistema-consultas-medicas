package com.agendador.api_agendador.web.mapper;

import com.agendador.api_agendador.entity.Appointment;
import com.agendador.api_agendador.entity.DoctorSchedule;
import com.agendador.api_agendador.web.dto.DoctorScheduleCreateDTO;
import com.agendador.api_agendador.web.dto.DoctorScheduleResponseDTO;
import com.agendador.api_agendador.web.dto.DoctorScheduleUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper
public interface DoctorScheduleMapper {

    DoctorScheduleMapper INSTANCE = Mappers.getMapper(DoctorScheduleMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctor.id", source = "doctorId")
    @Mapping(target = "appointments", ignore = true)
    DoctorSchedule toEntity(DoctorScheduleCreateDTO dto);

    @Mapping(target = "doctorId", source = "doctor.id")
    @Mapping(target = "appointmentIds", source = "appointments", qualifiedByName = "appointmentsToIds")
    DoctorScheduleResponseDTO toDto(DoctorSchedule doctorSchedule);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctor.id", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    void updateDto(DoctorScheduleUpdateDTO dto, @MappingTarget DoctorSchedule doctorSchedule);

    @Named("appointmentsToIds")
    default Set<UUID> appointmentsToIds(Set<Appointment> appointments) {
        if (appointments == null) {
            return Set.of();
        }
        return appointments.stream()
                .map(Appointment::getId)
                .collect(Collectors.toSet());
    }
}
