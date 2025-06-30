package com.agendador.api_agendador.web.dto.doctor_schedule;

import com.agendador.api_agendador.entity.enums.DayOfWeek;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

@Schema(description = "Data returned for a doctor's schedule")
public record DoctorScheduleResponseDTO(

        @Schema(description = "Schedule ID", example = "1")
        Long id,

        @Schema(description = "Doctor name", example = "Dr. John Smith")
        String doctorName,

        @Schema(description = "Day of the week", example = "MONDAY")
        DayOfWeek dayOfWeek,

        @Schema(description = "Start time", example = "09:00")
        LocalTime startTime,

        @Schema(description = "End time", example = "12:00")
        LocalTime endTime,

        @Schema(description = "Appointment IDs")
        Set<UUID> appointmentIds
) {}