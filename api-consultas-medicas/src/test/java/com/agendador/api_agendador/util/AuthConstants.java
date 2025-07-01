package com.agendador.api_agendador.util;

import com.agendador.api_agendador.entity.User;
import com.agendador.api_agendador.entity.enums.Role;
import com.agendador.api_agendador.web.dto.auth.LoginRequestDTO;
import com.agendador.api_agendador.web.dto.auth.LoginResponseDTO;
import com.agendador.api_agendador.web.dto.auth.RegisterResponseDTO;
import com.agendador.api_agendador.web.dto.auth.UserCreateDTO;

import java.time.Instant;
import java.time.LocalDate;

public final class AuthConstants {

    public static final String EMAIL = "auth.user@example.com";
    public static final String PASSWORD_RAW = "12345678";
    public static final String PASSWORD_ENCODED = "encodedPasswordAuth";

    public static final Long USER_ID = 99L;
    public static final String TOKEN_TYPE = "Bearer";
    public static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";
    public static final Instant EXPIRES_AT = Instant.parse("2025-12-31T23:59:59Z");

    public static final UserCreateDTO USER_CREATE_DTO = new UserCreateDTO(
            "Auth User",
            EMAIL,
            PASSWORD_RAW,
            "(99)99999-9999",
            LocalDate.of(1995, 10, 10)
    );

    public static final LoginRequestDTO LOGIN_REQUEST_DTO = new LoginRequestDTO(
            EMAIL,
            PASSWORD_RAW
    );

    public static final LoginResponseDTO LOGIN_RESPONSE_DTO = new LoginResponseDTO(
            USER_ID,
            TOKEN_TYPE,
            TOKEN,
            "USER",
            EXPIRES_AT
    );

    public static final RegisterResponseDTO REGISTER_RESPONSE_DTO = new RegisterResponseDTO(
            USER_ID,
            TOKEN_TYPE,
            TOKEN,
            EXPIRES_AT
    );

    public static User freshUserEntity() {
        return User.builder()
                .id(USER_ID)
                .name("Auth User")
                .email(EMAIL)
                .phone("(99)99999-9999")
                .password(PASSWORD_ENCODED)
                .birthDate(LocalDate.of(1995, 10, 10))
                .role(Role.USER)
                .build();
    }
}