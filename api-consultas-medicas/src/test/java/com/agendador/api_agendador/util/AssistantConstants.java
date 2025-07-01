package com.agendador.api_agendador.util;

import com.agendador.api_agendador.entity.Assistant;
import com.agendador.api_agendador.entity.User;
import com.agendador.api_agendador.entity.enums.Role;
import com.agendador.api_agendador.web.dto.assistant.AssistantCreateDTO;
import com.agendador.api_agendador.web.dto.assistant.AssistantResponseDTO;
import com.agendador.api_agendador.web.dto.assistant.AssistantUpdateDTO;

import java.time.LocalDate;

public final class AssistantConstants {

    public static final Long ASSISTANT_ID = 4L;
    public static final String ASSISTANT_NAME = "Ana Maria";
    public static final String ASSISTANT_EMAIL = "ana.maria@clinic.com";
    public static final String ASSISTANT_PHONE = "(33)98765-4321";
    public static final LocalDate ASSISTANT_BIRTHDATE = LocalDate.of(1988, 7, 15);
    public static final Role ASSISTANT_ROLE = Role.ASSISTANT;
    public static final String REGISTRATION_NUMBER = "REC20250001";

    public static final AssistantCreateDTO ASSISTANT_CREATE_DTO = new AssistantCreateDTO(
            REGISTRATION_NUMBER
    );

    public static final AssistantUpdateDTO ASSISTANT_UPDATE_DTO = new AssistantUpdateDTO(
            REGISTRATION_NUMBER
    );

    public static final AssistantResponseDTO ASSISTANT_RESPONSE_DTO = new AssistantResponseDTO(
            ASSISTANT_ID,
            ASSISTANT_NAME,
            REGISTRATION_NUMBER,
            ASSISTANT_EMAIL,
            ASSISTANT_PHONE,
            ASSISTANT_BIRTHDATE,
            ASSISTANT_ROLE.name()
    );

    public static Assistant freshAssistantEntity() {
        return Assistant.builder()
                .id(ASSISTANT_ID)
                .user(freshAssistantUser())
                .registrationNumber(REGISTRATION_NUMBER)
                .build();
    }

    public static User freshAssistantUser() {
        return User.builder()
                .id(ASSISTANT_ID)
                .name(ASSISTANT_NAME)
                .email(ASSISTANT_EMAIL)
                .phone(ASSISTANT_PHONE)
                .password("encryptedPassword")
                .birthDate(ASSISTANT_BIRTHDATE)
                .role(ASSISTANT_ROLE)
                .build();
    }
}