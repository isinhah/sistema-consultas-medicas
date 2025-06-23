package com.agendador.api_agendador.web.dto.appointment;

import com.agendador.api_agendador.entity.Appointment;
import org.mapstruct.*;
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

    @Mapping(source = "doctorSchedule.id", target = "doctorScheduleId")
    @Mapping(source = "patient.user.name", target = "patientName")
    @Mapping(source = "doctorSchedule.doctor.user.name", target = "doctorName")
    @Mapping(source = "assistant.user.name", target = "assistantName")
    AppointmentResponseDTO toDto(Appointment appointment);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            unmappedTargetPolicy = ReportingPolicy.IGNORE
    )
    @Mapping(source = "status", target = "status")
    void updateStatusFromDto(AppointmentUpdateStatusDTO dto, @MappingTarget Appointment appointment);
}