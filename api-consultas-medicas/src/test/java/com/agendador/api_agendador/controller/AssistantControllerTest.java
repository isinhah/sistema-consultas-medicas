package com.agendador.api_agendador.controller;

import com.agendador.api_agendador.security.JwtAuthenticationFilter;
import com.agendador.api_agendador.security.JwtTokenService;
import com.agendador.api_agendador.service.AssistantService;
import com.agendador.api_agendador.util.AssistantConstants;
import com.agendador.api_agendador.web.controller.AssistantController;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AssistantController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AssistantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AssistantService assistantService;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @DisplayName("GET /api/v1/assistants/{id} - should return assistant when found")
    void findById_ShouldReturnAssistant() throws Exception {
        var assistantDto = AssistantConstants.ASSISTANT_RESPONSE_DTO;

        when(assistantService.findById(assistantDto.id())).thenReturn(assistantDto);

        mockMvc.perform(get("/api/v1/assistants/{id}", assistantDto.id())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(assistantDto.id()))
                .andExpect(jsonPath("$.name").value(assistantDto.name()));
    }

    @Test
    @DisplayName("GET /api/v1/assistants - should return page of assistants")
    void findAll_ShouldReturnPage() throws Exception {
        var assistantDto = AssistantConstants.ASSISTANT_RESPONSE_DTO;
        var page = new PageImpl<>(List.of(assistantDto));

        when(assistantService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/assistants")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(assistantDto.id()))
                .andExpect(jsonPath("$.content[0].name").value(assistantDto.name()));
    }

    @Test
    @DisplayName("GET /api/v1/assistants/by-registration-number - should return assistant by registration number")
    void findByRegistrationNumber_ShouldReturnAssistant() throws Exception {
        var assistantDto = AssistantConstants.ASSISTANT_RESPONSE_DTO;
        String regNumber = assistantDto.registrationNumber();

        when(assistantService.findByRegistrationNumber(regNumber)).thenReturn(assistantDto);

        mockMvc.perform(get("/api/v1/assistants/by-registration-number")
                        .param("registrationNumber", regNumber))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(assistantDto.id()))
                .andExpect(jsonPath("$.registrationNumber").value(regNumber));
    }

    @Test
    @DisplayName("PATCH /api/v1/assistants/{id} - should update assistant and return updated response")
    void update_ShouldReturnUpdatedAssistant() throws Exception {
        var updateDto = AssistantConstants.ASSISTANT_UPDATE_DTO;
        var assistantDto = AssistantConstants.ASSISTANT_RESPONSE_DTO;

        when(assistantService.update(eq(assistantDto.id()), any())).thenReturn(assistantDto);

        mockMvc.perform(patch("/api/v1/assistants/{id}", assistantDto.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(assistantDto.id()))
                .andExpect(jsonPath("$.name").value(assistantDto.name()));
    }
}
