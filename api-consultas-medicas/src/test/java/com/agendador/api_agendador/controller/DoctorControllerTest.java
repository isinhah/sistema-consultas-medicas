package com.agendador.api_agendador.controller;

import com.agendador.api_agendador.security.JwtAuthenticationFilter;
import com.agendador.api_agendador.security.JwtTokenService;
import com.agendador.api_agendador.service.DoctorService;
import com.agendador.api_agendador.util.DoctorConstants;
import com.agendador.api_agendador.web.controller.DoctorController;
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


@WebMvcTest(DoctorController.class)
@AutoConfigureMockMvc(addFilters = false)
public class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DoctorService doctorService;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @DisplayName("GET /api/v1/doctors/{id} - should return doctor when found")
    void findById_ShouldReturnDoctor() throws Exception {
        var doctorDto = DoctorConstants.DOCTOR_RESPONSE_DTO;

        when(doctorService.findById(doctorDto.id())).thenReturn(doctorDto);

        mockMvc.perform(get("/api/v1/doctors/{id}", doctorDto.id())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(doctorDto.id()))
                .andExpect(jsonPath("$.name").value(doctorDto.name()));
    }

    @Test
    @DisplayName("GET /api/v1/doctors - should return page of doctors")
    void findAll_ShouldReturnPage() throws Exception {
        var doctorDto = DoctorConstants.DOCTOR_RESPONSE_DTO;
        var page = new PageImpl<>(List.of(doctorDto));

        when(doctorService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/doctors")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(doctorDto.id()))
                .andExpect(jsonPath("$.content[0].name").value(doctorDto.name()));
    }

    @Test
    @DisplayName("GET /api/v1/doctors/by-crm - should return doctor by CRM")
    void findByCrm_ShouldReturnDoctor() throws Exception {
        var doctorDto = DoctorConstants.DOCTOR_RESPONSE_DTO;
        String crm = doctorDto.crm();

        when(doctorService.findByCrm(crm)).thenReturn(doctorDto);

        mockMvc.perform(get("/api/v1/doctors/by-crm")
                        .param("crm", crm))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(doctorDto.id()))
                .andExpect(jsonPath("$.crm").value(doctorDto.crm()));
    }

    @Test
    @DisplayName("PATCH /api/v1/doctors/{id} - should update doctor and return updated response")
    void update_ShouldReturnUpdatedDoctor() throws Exception {
        var updateDto = DoctorConstants.DOCTOR_UPDATE_DTO;
        var doctorDto = DoctorConstants.DOCTOR_RESPONSE_DTO;

        when(doctorService.update(eq(doctorDto.id()), any())).thenReturn(doctorDto);

        mockMvc.perform(patch("/api/v1/doctors/{id}", doctorDto.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(doctorDto.id()))
                .andExpect(jsonPath("$.name").value(doctorDto.name()));
    }
}