package com.agendador.api_agendador.util;

import com.agendador.api_agendador.entity.Appointment;
import com.agendador.api_agendador.entity.Assistant;
import com.agendador.api_agendador.entity.DoctorSchedule;
import com.agendador.api_agendador.entity.Patient;
import com.agendador.api_agendador.entity.enums.AppointmentStatus;
import com.agendador.api_agendador.web.dto.appointment.AppointmentBookDTO;
import com.agendador.api_agendador.web.dto.appointment.AppointmentCreateSlotDTO;
import com.agendador.api_agendador.web.dto.appointment.AppointmentResponseDTO;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.agendador.api_agendador.util.AssistantConstants.ASSISTANT_ID;
import static com.agendador.api_agendador.util.DoctorConstants.DOCTOR_NAME;
import static com.agendador.api_agendador.util.DoctorScheduleConstants.SCHEDULE_ID;
import static com.agendador.api_agendador.util.PatientConstants.PATIENT_ID;
import static com.agendador.api_agendador.util.PatientConstants.PATIENT_NAME;

public final class AppointmentConstants {

    public static final UUID APPOINTMENT_ID = UUID.fromString("c1a8c21f-3b9e-4fcb-b2a7-643fd7b11d2b");
    public static final LocalDateTime APPOINTMENT_DATETIME = LocalDateTime.of(2025, 8, 11, 10, 0);
    public static final AppointmentStatus APPOINTMENT_STATUS = AppointmentStatus.BOOKED;

    public static final AppointmentCreateSlotDTO APPOINTMENT_CREATE_SLOT_DTO = new AppointmentCreateSlotDTO(
            SCHEDULE_ID, APPOINTMENT_DATETIME
    );

    public static final AppointmentBookDTO APPOINTMENT_BOOK_DTO = new AppointmentBookDTO(
            APPOINTMENT_ID
    );

    public static final AppointmentResponseDTO APPOINTMENT_RESPONSE_DTO =
            new AppointmentResponseDTO(
                    APPOINTMENT_ID,
                    PATIENT_NAME,
                    DOCTOR_NAME,
                    APPOINTMENT_STATUS,
                    APPOINTMENT_DATETIME
            );

    public static Appointment freshAppointmentEntity() {
        Appointment appointment = new Appointment();
        appointment.setId(APPOINTMENT_ID);
        appointment.setStatus(APPOINTMENT_STATUS);
        appointment.setDateTime(APPOINTMENT_DATETIME);

        Assistant assistant = new Assistant();
        assistant.setId(ASSISTANT_ID);
        appointment.setAssistant(assistant);

        DoctorSchedule doctorSchedule = new DoctorSchedule();
        doctorSchedule.setId(SCHEDULE_ID);
        appointment.setDoctorSchedule(doctorSchedule);

        Patient patient = new Patient();
        patient.setId(PATIENT_ID);
        appointment.setPatient(patient);

        return appointment;
    }
}
