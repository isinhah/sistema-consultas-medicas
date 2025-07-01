package com.agendador.api_agendador.controller;

import com.agendador.api_agendador.security.JwtAuthenticationFilter;
import com.agendador.api_agendador.security.JwtTokenService;
import com.agendador.api_agendador.service.UserService;
import com.agendador.api_agendador.util.UserConstants;
import com.agendador.api_agendador.web.controller.UserController;
import com.agendador.api_agendador.web.dto.user.UserResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @DisplayName("GET /api/v1/users/{id} - should return user when found")
    void shouldReturnUser_WhenFound() throws Exception {
        Long userId = UserConstants.USER_ID;
        UserResponseDTO responseDTO = UserConstants.USER_RESPONSE_DTO;

        when(userService.findById(userId)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/users/{id}", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value(responseDTO.name()))
                .andExpect(jsonPath("$.email").value(responseDTO.email()));
    }

    @Test
    @DisplayName("GET /api/v1/users - should return paged list of users")
    void findAll_ShouldReturnPage() throws Exception {
        Pageable pageable = Pageable.ofSize(10);
        var userDto = UserConstants.USER_RESPONSE_DTO;
        var page = new PageImpl<>(List.of(userDto), pageable, 1);

        when(userService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/users")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(userDto.id()))
                .andExpect(jsonPath("$.content[0].name").value(userDto.name()));
    }

    @Test
    @DisplayName("GET /api/v1/users/search - should return users filtered by name")
    void findByName_ShouldReturnPage() throws Exception {
        Pageable pageable = Pageable.ofSize(10);
        var userDto = UserConstants.USER_RESPONSE_DTO;
        var page = new PageImpl<>(List.of(userDto), pageable, 1);

        when(userService.findByName(eq("John"), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/users/search")
                        .param("name", "John")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(userDto.id()))
                .andExpect(jsonPath("$.content[0].name").value(userDto.name()));
    }

    @Test
    @DisplayName("GET /api/v1/users/role - should return users filtered by role")
    void findByRole_ShouldReturnPage() throws Exception {
        Pageable pageable = Pageable.ofSize(10);
        var userDto = UserConstants.USER_RESPONSE_DTO;
        var page = new PageImpl<>(List.of(userDto), pageable, 1);

        when(userService.findByRole(eq("ADMIN"), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/users/role")
                        .param("role", "ADMIN")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(userDto.id()))
                .andExpect(jsonPath("$.content[0].name").value(userDto.name()));
    }

    @Test
    @DisplayName("GET /api/v1/users/by-contact - should return user filtered by email or phone")
    void findByEmailOrPhone_ShouldReturnUser() throws Exception {
        var userDto = UserConstants.USER_RESPONSE_DTO;

        when(userService.findByEmailOrPhone(eq("john@example.com"), isNull())).thenReturn(userDto);

        mockMvc.perform(get("/api/v1/users/by-contact")
                        .param("email", "john@example.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.id()))
                .andExpect(jsonPath("$.name").value(userDto.name()));
    }

    @Test
    @DisplayName("PUT /api/v1/users/{id} - should update and return updated user")
    void update_ShouldReturnUpdatedUser() throws Exception {
        var userDto = UserConstants.USER_RESPONSE_DTO;
        var updateDto = UserConstants.USER_UPDATE_DTO;

        when(userService.update(eq(userDto.id()), any())).thenReturn(userDto);

        mockMvc.perform(put("/api/v1/users/{id}", userDto.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.id()))
                .andExpect(jsonPath("$.name").value(userDto.name()));
    }

    @Test
    @DisplayName("PUT /api/v1/users/{userId}/password - should update user password and return no content")
    void updatePassword_ShouldReturnNoContent() throws Exception {
        var userId = UserConstants.USER_ID;
        var passwordDto = UserConstants.PASSWORD_UPDATE_DTO;

        doNothing().when(userService).updatePassword(eq(userId), any());

        mockMvc.perform(put("/api/v1/users/{userId}/password", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordDto)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/v1/users/{id} - should delete user and return no content")
    void delete_ShouldReturnNoContent() throws Exception {
        var userId = UserConstants.USER_ID;

        doNothing().when(userService).delete(userId);

        mockMvc.perform(delete("/api/v1/users/{id}", userId))
                .andExpect(status().isNoContent());
    }
}
