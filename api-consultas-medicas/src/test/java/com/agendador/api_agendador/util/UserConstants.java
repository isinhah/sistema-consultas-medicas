package com.agendador.api_agendador.util;

import com.agendador.api_agendador.entity.User;
import com.agendador.api_agendador.entity.enums.Role;
import com.agendador.api_agendador.web.dto.user.PasswordUpdateDTO;
import com.agendador.api_agendador.web.dto.user.UserResponseDTO;
import com.agendador.api_agendador.web.dto.user.UserUpdateDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class UserConstants {

    private UserConstants() {}

    public static final Long USER_ID = 1L;
    public static final String USER_NAME = "John Doe";
    public static final String USER_EMAIL = "john.doe@example.com";
    public static final String USER_PHONE = "(11)91234-5678";
    public static final String USER_PASSWORD_RAW = "12345678";
    public static final String USER_PASSWORD_ENCODED = "encoded_password";
    public static final LocalDate USER_BIRTHDATE = LocalDate.of(1990, 5, 20);
    public static final LocalDateTime CREATED_AT = LocalDateTime.of(2023, 7, 20, 14, 55, 33);
    public static final LocalDateTime UPDATED_AT = LocalDateTime.of(2023, 7, 21, 9, 12, 5);
    public static final Role USER_ROLE = Role.ADMIN;

    public static final UserUpdateDTO USER_UPDATE_DTO = new UserUpdateDTO(
            "João Silva",
            "joao.silva@example.com",
            "(11) 91234-5678",
            USER_BIRTHDATE
    );

    public static final PasswordUpdateDTO PASSWORD_UPDATE_DTO = new PasswordUpdateDTO(
            USER_PASSWORD_RAW,
            "87654321"
    );

    public static final UserResponseDTO USER_RESPONSE_DTO = new UserResponseDTO(
            USER_ID,
            USER_NAME,
            USER_EMAIL,
            USER_PHONE,
            USER_BIRTHDATE,
            USER_ROLE.name(),
            CREATED_AT,
            UPDATED_AT
    );

    public static final UserResponseDTO USER_RESPONSE_DTO_WITHOUT_METADATA = new UserResponseDTO(
            1L,
            "John Doe",
            "john.doe@example.com",
            "(11)91234-5678",
            LocalDate.of(1990, 5, 20),
            null,
            null,
            null
    );

    public static final User USER_ENTITY = User.builder()
            .id(USER_ID)
            .name(USER_NAME)
            .email(USER_EMAIL)
            .password(USER_PASSWORD_ENCODED)
            .phone(USER_PHONE)
            .birthDate(USER_BIRTHDATE)
            .createdAt(CREATED_AT)
            .updatedAt(UPDATED_AT)
            .role(USER_ROLE)
            .build();
}