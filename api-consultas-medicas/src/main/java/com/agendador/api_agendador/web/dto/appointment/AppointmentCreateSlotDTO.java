package com.agendador.api_agendador.web.dto.appointment;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AppointmentCreateSlotDTO(
        @NotNull(message = "DoctorSchedule ID must not be null")
        Long doctorScheduleId,

        @NotNull(message = "Date and time must not be null")
        @Future(message = "Date and time must be in the future")
        LocalDateTime dateTime
) {}