package com.agendador.api_agendador.util;

import com.agendador.api_agendador.web.dto.auth.LoginRequestDTO;
import com.agendador.api_agendador.web.dto.auth.LoginResponseDTO;
import com.agendador.api_agendador.web.dto.auth.RegisterResponseDTO;
import com.agendador.api_agendador.web.dto.auth.UserCreateDTO;

import java.time.Instant;
import java.time.LocalDate;

public class AuthConstants {

    public static final UserCreateDTO USER_CREATE_DTO = new UserCreateDTO(
            "John Doe",
            "john.doe@example.com",
            "12345678",
            "(11)91234-5678",
            LocalDate.of(1990, 5, 20)
    );

    public static final String EMAIL = "john.doe@example.com";
    public static final String PASSWORD = "12345678";

    public static final Long USER_ID = 1L;
    public static final String TOKEN_TYPE = "Bearer";
    public static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";
    public static final String ROLE = "USER";
    public static final Instant EXPIRES_AT = Instant.parse("2025-12-31T23:59:59Z");

    public static final LoginRequestDTO LOGIN_REQUEST_DTO = new LoginRequestDTO(
            EMAIL,
            PASSWORD
    );

    public static final LoginResponseDTO LOGIN_RESPONSE_DTO = new LoginResponseDTO(
            USER_ID,
            TOKEN_TYPE,
            TOKEN,
            ROLE,
            EXPIRES_AT
    );

    public static final RegisterResponseDTO REGISTER_RESPONSE_DTO = new RegisterResponseDTO(
            USER_ID,
            TOKEN_TYPE,
            TOKEN,
            EXPIRES_AT
    );
}