package com.agendador.api_agendador.web.dto.appointment;

import com.agendador.api_agendador.entity.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AppointmentMapper {

    AppointmentMapper INSTANCE = Mappers.getMapper(AppointmentMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "dateTime", target = "dateTime")
    @Mapping(source = "doctorScheduleId", target = "doctorSchedule.id")
    @Mapping(target = "assistant", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "status", ignore = true)
    Appointment toEntity(AppointmentCreateSlotDTO dto);

    @Mapping(source = "patient.user.name", target = "patientName")
    @Mapping(source = "doctorSchedule.doctor.user.name", target = "doctorName")
    AppointmentResponseDTO toDto(Appointment appointment);
}