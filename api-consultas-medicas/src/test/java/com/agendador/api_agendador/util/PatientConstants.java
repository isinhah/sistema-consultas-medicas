package com.agendador.api_agendador.util;

import com.agendador.api_agendador.entity.Patient;
import com.agendador.api_agendador.entity.User;
import com.agendador.api_agendador.entity.enums.Role;
import com.agendador.api_agendador.web.dto.patient.PatientCreateDTO;
import com.agendador.api_agendador.web.dto.patient.PatientResponseDTO;
import com.agendador.api_agendador.web.dto.patient.PatientUpdateDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class PatientConstants {

    public static final Long PATIENT_ID = 2L;
    public static final String PATIENT_NAME = "João";
    public static final String PATIENT_EMAIL = "joao@example.com";
    public static final String PATIENT_PHONE = "(11)912345678";
    public static final String PATIENT_PASSWORD_ENCODED = "encryptedPassword";
    public static final LocalDate PATIENT_BIRTHDATE = LocalDate.of(1990, 5, 20);
    public static final LocalDateTime PATIENT_CREATED_AT = LocalDateTime.of(2023, 7, 20, 14, 55, 33);
    public static final LocalDateTime PATIENT_UPDATED_AT = LocalDateTime.of(2023, 7, 21, 9, 12, 5);
    public static final Role PATIENT_ROLE = Role.PATIENT;
    public static final String CPF = "12345678901";

    public static final PatientCreateDTO PATIENT_CREATE_DTO = new PatientCreateDTO(CPF);

    public static final PatientUpdateDTO PATIENT_UPDATE_DTO = new PatientUpdateDTO(CPF);

    public static final PatientResponseDTO PATIENT_RESPONSE_DTO = new PatientResponseDTO(
            PATIENT_ID,
            PATIENT_NAME,
            CPF,
            PATIENT_EMAIL,
            PATIENT_PHONE,
            PATIENT_BIRTHDATE,
            PATIENT_ROLE.name()
    );

    public static Patient freshPatientEntity() {
        return Patient.builder()
                .id(PATIENT_ID)
                .user(freshPatientUser())
                .cpf(CPF)
                .build();
    }

    public static User freshPatientUser() {
        return User.builder()
                .id(PATIENT_ID)
                .name(PATIENT_NAME)
                .email(PATIENT_EMAIL)
                .phone(PATIENT_PHONE)
                .password(PATIENT_PASSWORD_ENCODED)
                .birthDate(PATIENT_BIRTHDATE)
                .createdAt(PATIENT_CREATED_AT)
                .updatedAt(PATIENT_UPDATED_AT)
                .role(PATIENT_ROLE)
                .build();
    }
}
