package com.agendador.api_agendador.util;

import com.agendador.api_agendador.entity.Doctor;
import com.agendador.api_agendador.entity.DoctorSchedule;
import com.agendador.api_agendador.entity.enums.DayOfWeek;
import com.agendador.api_agendador.web.dto.doctor_schedule.DoctorScheduleCreateDTO;
import com.agendador.api_agendador.web.dto.doctor_schedule.DoctorScheduleResponseDTO;
import com.agendador.api_agendador.web.dto.doctor_schedule.DoctorScheduleUpdateDTO;

import java.time.LocalTime;
import java.util.HashSet;

import static com.agendador.api_agendador.util.DoctorConstants.DOCTOR_NAME;

public final class DoctorScheduleConstants {

    public static final Long SCHEDULE_ID = 1L;
    public static final Long DOCTOR_ID = 3L;

    public static final DayOfWeek DAY_OF_WEEK_CREATE = DayOfWeek.MONDAY;
    public static final DayOfWeek DAY_OF_WEEK_UPDATE = DayOfWeek.TUESDAY;

    public static final LocalTime START_TIME_CREATE = LocalTime.of(9, 0);
    public static final LocalTime END_TIME_CREATE = LocalTime.of(12, 0);

    public static final LocalTime START_TIME_UPDATE = LocalTime.of(14, 0);
    public static final LocalTime END_TIME_UPDATE = LocalTime.of(18, 0);

    public static final DoctorScheduleCreateDTO DOCTOR_SCHEDULE_CREATE_DTO = new DoctorScheduleCreateDTO(
            DOCTOR_ID,
            DAY_OF_WEEK_CREATE,
            START_TIME_CREATE,
            END_TIME_CREATE
    );

    public static final DoctorScheduleUpdateDTO DOCTOR_SCHEDULE_UPDATE_DTO = new DoctorScheduleUpdateDTO(
            DAY_OF_WEEK_UPDATE,
            START_TIME_UPDATE,
            END_TIME_UPDATE
    );

    public static final DoctorScheduleResponseDTO DOCTOR_SCHEDULE_RESPONSE_DTO = new DoctorScheduleResponseDTO(
            SCHEDULE_ID,
            DOCTOR_NAME,
            DAY_OF_WEEK_CREATE,
            START_TIME_CREATE,
            END_TIME_CREATE
    );

    public static DoctorSchedule freshDoctorScheduleEntity() {
        DoctorSchedule ds = new DoctorSchedule();
        ds.setId(SCHEDULE_ID);

        Doctor doctor = new Doctor();
        doctor.setId(DOCTOR_ID);
        ds.setDoctor(doctor);

        ds.setDayOfWeek(DAY_OF_WEEK_CREATE);
        ds.setStartTime(START_TIME_CREATE);
        ds.setEndTime(END_TIME_CREATE);
        ds.setAppointments(new HashSet<>());

        return ds;
    }
}
