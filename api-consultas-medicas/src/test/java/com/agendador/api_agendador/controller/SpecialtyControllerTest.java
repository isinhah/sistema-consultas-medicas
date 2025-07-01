package com.agendador.api_agendador.controller;

import com.agendador.api_agendador.security.JwtAuthenticationFilter;
import com.agendador.api_agendador.security.JwtTokenService;
import com.agendador.api_agendador.service.SpecialtyService;
import com.agendador.api_agendador.web.controller.SpecialtyController;
import com.agendador.api_agendador.web.dto.specialty.SpecialtyCreateDTO;
import com.agendador.api_agendador.web.dto.specialty.SpecialtyResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.agendador.api_agendador.util.SpecialtyConstants.SPECIALTY_CREATE_DTO;
import static com.agendador.api_agendador.util.SpecialtyConstants.SPECIALTY_RESPONSE_DTO_CARDIOLOGY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SpecialtyController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SpecialtyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SpecialtyService specialtyService;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @DisplayName("GET /api/v1/specialties/{id} - should return specialty when found")
    void findById_ShouldReturnSpecialty() throws Exception {
        Long id = 1L;
        SpecialtyResponseDTO dto = new SpecialtyResponseDTO(id, "Cardiology");

        when(specialtyService.findById(id)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/specialties/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Cardiology"));
    }

    @Test
    @DisplayName("GET /api/v1/specialties - should return paged specialties")
    void findAll_ShouldReturnPage() throws Exception {
        SpecialtyResponseDTO dto = new SpecialtyResponseDTO(1L, "Cardiology");
        Page<SpecialtyResponseDTO> page = new PageImpl<>(List.of(dto));

        when(specialtyService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/specialties")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(dto.id()))
                .andExpect(jsonPath("$.content[0].name").value(dto.name()));
    }

    @Test
    @DisplayName("GET /api/v1/specialties/search - should return specialty by name")
    void findByName_ShouldReturnSpecialty() throws Exception {
        SpecialtyResponseDTO dto = new SpecialtyResponseDTO(1L, "Cardiology");

        when(specialtyService.findByName("Cardiology")).thenReturn(dto);

        mockMvc.perform(get("/api/v1/specialties/search")
                        .param("name", "Cardiology")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.id()))
                .andExpect(jsonPath("$.name").value(dto.name()));
    }

    @Test
    @DisplayName("GET /api/v1/specialties/by-doctor-id/{doctorId} - should return specialties page")
    void findByDoctorId_ShouldReturnPage() throws Exception {
        Long doctorId = 10L;
        SpecialtyResponseDTO dto = new SpecialtyResponseDTO(1L, "Cardiology");
        Page<SpecialtyResponseDTO> page = new PageImpl<>(List.of(dto));

        when(specialtyService.findByDoctorId(eq(doctorId), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/specialties/by-doctor-id/{doctorId}", doctorId)
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(dto.id()))
                .andExpect(jsonPath("$.content[0].name").value(dto.name()));
    }

    @Test
    @DisplayName("GET /api/v1/specialties/by-doctor-crm - should return specialties page")
    void findByDoctorCrm_ShouldReturnPage() throws Exception {
        String crm = "CRM123456";
        SpecialtyResponseDTO dto = new SpecialtyResponseDTO(1L, "Cardiology");
        Page<SpecialtyResponseDTO> page = new PageImpl<>(List.of(dto));

        when(specialtyService.findByDoctorCrm(eq(crm), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/specialties/by-doctor-crm")
                        .param("crm", crm)
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(dto.id()))
                .andExpect(jsonPath("$.content[0].name").value(dto.name()));
    }

    @Test
    @DisplayName("POST /api/v1/specialties - should create specialty and return it")
    void create_ShouldReturnCreatedSpecialty() throws Exception {
        SpecialtyCreateDTO createDTO = SPECIALTY_CREATE_DTO;
        SpecialtyResponseDTO responseDTO = SPECIALTY_RESPONSE_DTO_CARDIOLOGY;

        when(specialtyService.create(any(SpecialtyCreateDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/specialties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDTO.id()))
                .andExpect(jsonPath("$.name").value(responseDTO.name()));
    }

    @Test
    @DisplayName("DELETE /api/v1/specialties/{id} - should delete specialty and return no content")
    void delete_ShouldReturnNoContent() throws Exception {
        Long id = 1L;
        doNothing().when(specialtyService).delete(id);

        mockMvc.perform(delete("/api/v1/specialties/{id}", id))
                .andExpect(status().isNoContent());
    }
}
