package com.agendador.api_agendador.controller;

import com.agendador.api_agendador.security.JwtAuthenticationFilter;
import com.agendador.api_agendador.security.JwtTokenService;
import com.agendador.api_agendador.service.AuthenticationService;
import com.agendador.api_agendador.util.UserConstants;
import com.agendador.api_agendador.web.controller.AuthenticationController;
import com.agendador.api_agendador.web.dto.auth.LoginResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.agendador.api_agendador.util.AuthConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthenticationService authenticationService;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @DisplayName("POST /api/v1/auth/register - should register a user and return 201 with response")
    void register_ShouldReturnCreatedWithResponse() throws Exception {
        var userCreateDTO = USER_CREATE_DTO;
        var responseDTO = REGISTER_RESPONSE_DTO;

        when(authenticationService.register(any())).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(responseDTO.userId()))
                .andExpect(jsonPath("$.tokenType").value(responseDTO.tokenType()))
                .andExpect(jsonPath("$.token").value(responseDTO.token()))
                .andExpect(jsonPath("$.expiresAt").value(responseDTO.expiresAt().toString()));
    }

    @Test
    @DisplayName("POST /api/v1/auth/login - should login user and return 200 with token")
    void login_ShouldReturnToken_WhenCredentialsValid() throws Exception {
        LoginResponseDTO responseDTO = LOGIN_RESPONSE_DTO;

        when(authenticationService.login(any())).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(LOGIN_REQUEST_DTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(responseDTO.token()));
    }

    @Test
    @DisplayName("PATCH /api/v1/auth/promote-to-admin/{userId} - should promote user to admin")
    void promoteToAdmin_ShouldReturnNoContent() throws Exception {
        Long userId = UserConstants.USER_ID;

        doNothing().when(authenticationService).promoteToAdmin(userId);

        mockMvc.perform(patch("/api/v1/auth/promote-to-admin/{userId}", userId))
                .andExpect(status().isNoContent());
    }
}
