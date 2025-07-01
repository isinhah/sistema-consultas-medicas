package com.agendador.api_agendador.util;

import com.agendador.api_agendador.entity.Doctor;
import com.agendador.api_agendador.entity.Specialty;
import com.agendador.api_agendador.entity.User;
import com.agendador.api_agendador.entity.enums.Role;
import com.agendador.api_agendador.web.dto.doctor.DoctorCreateDTO;
import com.agendador.api_agendador.web.dto.doctor.DoctorResponseDTO;
import com.agendador.api_agendador.web.dto.doctor.DoctorUpdateDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public final class DoctorConstants {

    public static final Long DOCTOR_ID = 3L;
    public static final String DOCTOR_NAME = "Dr. Mariana Costa";
    public static final String DOCTOR_EMAIL = "mariana.costa@clinic.com";
    public static final String DOCTOR_PHONE = "(22)91234-5678";
    public static final String DOCTOR_PASSWORD_ENCODED = "encryptedPassword";
    public static final LocalDate DOCTOR_BIRTHDATE = LocalDate.of(1982, 11, 3);
    public static final LocalDateTime DOCTOR_CREATED_AT = LocalDateTime.of(2023, 7, 22, 10, 15, 0);
    public static final LocalDateTime DOCTOR_UPDATED_AT = LocalDateTime.of(2023, 7, 23, 14, 30, 0);
    public static final Role DOCTOR_ROLE = Role.DOCTOR;
    public static final String CRM_CREATE = "SP123456";
    public static final String CRM_UPDATE = "MG432198";
    public static final Set<Long> SPECIALTY_IDS_CREATE = Set.of(1L);
    public static final Set<Long> SPECIALTY_IDS_UPDATE = Set.of(1L, 2L);

    public static final DoctorCreateDTO DOCTOR_CREATE_DTO = new DoctorCreateDTO(
            CRM_CREATE,
            SPECIALTY_IDS_CREATE
    );

    public static final DoctorUpdateDTO DOCTOR_UPDATE_DTO = new DoctorUpdateDTO(
            CRM_UPDATE,
            SPECIALTY_IDS_UPDATE
    );

    public static final DoctorResponseDTO DOCTOR_RESPONSE_DTO = new DoctorResponseDTO(
            DOCTOR_ID,
            DOCTOR_NAME,
            CRM_CREATE,
            DOCTOR_EMAIL,
            DOCTOR_PHONE,
            DOCTOR_BIRTHDATE,
            DOCTOR_ROLE.name(),
            SPECIALTY_IDS_CREATE
    );

    public static User freshDoctorUser() {
        return User.builder()
                .id(DOCTOR_ID)
                .name(DOCTOR_NAME)
                .email(DOCTOR_EMAIL)
                .phone(DOCTOR_PHONE)
                .password(DOCTOR_PASSWORD_ENCODED)
                .birthDate(DOCTOR_BIRTHDATE)
                .createdAt(DOCTOR_CREATED_AT)
                .updatedAt(DOCTOR_UPDATED_AT)
                .role(DOCTOR_ROLE)
                .build();
    }

    public static Doctor freshDoctorEntity() {
        return Doctor.builder()
                .id(DOCTOR_ID)
                .user(freshDoctorUser())
                .crm(CRM_CREATE)
                .specialties(specialtiesFromIds(SPECIALTY_IDS_CREATE))
                .build();
    }

    public static Set<Specialty> specialtiesFromIds(Set<Long> ids) {
        return ids.stream()
                .map(id -> {
                    Specialty specialty = new Specialty();
                    specialty.setId(id);
                    return specialty;
                })
                .collect(Collectors.toSet());
    }
}