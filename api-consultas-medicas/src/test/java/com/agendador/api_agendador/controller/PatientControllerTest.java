package com.agendador.api_agendador.controller;

import com.agendador.api_agendador.security.JwtAuthenticationFilter;
import com.agendador.api_agendador.security.JwtTokenService;
import com.agendador.api_agendador.service.PatientService;
import com.agendador.api_agendador.util.PatientConstants;
import com.agendador.api_agendador.web.controller.PatientController;
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

@WebMvcTest(PatientController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PatientService patientService;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @DisplayName("GET /api/v1/patients/{id} - should return patient when found")
    void findById_ShouldReturnPatient() throws Exception {
        var patientDto = PatientConstants.PATIENT_RESPONSE_DTO;

        when(patientService.findById(patientDto.id())).thenReturn(patientDto);

        mockMvc.perform(get("/api/v1/patients/{id}", patientDto.id())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(patientDto.id()))
                .andExpect(jsonPath("$.name").value(patientDto.name()));
    }

    @Test
    @DisplayName("GET /api/v1/patients - should return page of patients")
    void findAll_ShouldReturnPage() throws Exception {
        var patientDto = PatientConstants.PATIENT_RESPONSE_DTO;
        var page = new PageImpl<>(List.of(patientDto));

        when(patientService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/patients")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(patientDto.id()))
                .andExpect(jsonPath("$.content[0].name").value(patientDto.name()));
    }

    @Test
    @DisplayName("GET /api/v1/patients/by-cpf - should return patient by CPF")
    void findByCpf_ShouldReturnPatient() throws Exception {
        var patientDto = PatientConstants.PATIENT_RESPONSE_DTO;
        String cpf = patientDto.cpf();

        when(patientService.findByCpf(cpf)).thenReturn(patientDto);

        mockMvc.perform(get("/api/v1/patients/by-cpf")
                        .param("cpf", cpf))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(patientDto.id()))
                .andExpect(jsonPath("$.cpf").value(patientDto.cpf()));
    }

    @Test
    @DisplayName("PATCH /api/v1/patients/{id} - should update patient and return updated response")
    void update_ShouldReturnUpdatedPatient() throws Exception {
        var updateDto = PatientConstants.PATIENT_UPDATE_DTO;
        var patientDto = PatientConstants.PATIENT_RESPONSE_DTO;

        when(patientService.update(eq(patientDto.id()), any())).thenReturn(patientDto);

        mockMvc.perform(patch("/api/v1/patients/{id}", patientDto.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(patientDto.id()))
                .andExpect(jsonPath("$.name").value(patientDto.name()));
    }
}
