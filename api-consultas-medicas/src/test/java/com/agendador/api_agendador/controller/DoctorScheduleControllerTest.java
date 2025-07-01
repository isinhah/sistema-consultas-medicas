package com.agendador.api_agendador.controller;

import com.agendador.api_agendador.security.JwtAuthenticationFilter;
import com.agendador.api_agendador.security.JwtTokenService;
import com.agendador.api_agendador.service.DoctorScheduleService;
import com.agendador.api_agendador.web.controller.DoctorScheduleController;
import com.agendador.api_agendador.web.dto.doctor_schedule.DoctorScheduleCreateDTO;
import com.agendador.api_agendador.web.dto.doctor_schedule.DoctorScheduleResponseDTO;
import com.agendador.api_agendador.web.dto.doctor_schedule.DoctorScheduleUpdateDTO;
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

import static com.agendador.api_agendador.util.DoctorScheduleConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DoctorScheduleController.class)
@AutoConfigureMockMvc(addFilters = false)
public class DoctorScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DoctorScheduleService doctorScheduleService;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @DisplayName("GET /api/v1/doctor-schedules/{id} - should return schedule by id")
    void findById_ShouldReturnSchedule() throws Exception {
        Long id = 1L;

        when(doctorScheduleService.findById(id)).thenReturn(DOCTOR_SCHEDULE_RESPONSE_DTO);

        mockMvc.perform(get("/api/v1/doctor-schedules/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    @DisplayName("GET /api/v1/doctor-schedules/search - should return schedules by search params")
    void searchSchedules_ShouldReturnPage() throws Exception {
        Long doctorId = 2L;
        DoctorScheduleResponseDTO dto = DOCTOR_SCHEDULE_RESPONSE_DTO;
        Page<DoctorScheduleResponseDTO> page = new PageImpl<>(List.of(dto));

        when(doctorScheduleService.findSchedulesByDoctorId(eq(doctorId), any(), any(), any(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/doctor-schedules/search")
                        .param("doctorId", doctorId.toString())
                        .param("dayOfWeek", "TUESDAY")
                        .param("startTime", "10:00:00")
                        .param("endTime", "13:00:00")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(dto.id()));
    }

    @Test
    @DisplayName("GET /api/v1/doctor-schedules/specialty/{specialtyId} - should return schedules by specialty")
    void findSchedulesBySpecialty_ShouldReturnPage() throws Exception {
        Long specialtyId = 3L;
        DoctorScheduleResponseDTO dto = DOCTOR_SCHEDULE_RESPONSE_DTO;
        Page<DoctorScheduleResponseDTO> page = new PageImpl<>(List.of(dto));

        when(doctorScheduleService.findAvailableSchedulesBySpecialty(eq(specialtyId), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/doctor-schedules/specialty/{specialtyId}", specialtyId)
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(dto.id()));
    }

    @Test
    @DisplayName("POST /api/v1/doctor-schedules - should create schedule and return it")
    void create_ShouldReturnCreatedSchedule() throws Exception {
        DoctorScheduleResponseDTO responseDTO = DOCTOR_SCHEDULE_RESPONSE_DTO;

        when(doctorScheduleService.create(any(DoctorScheduleCreateDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/doctor-schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(DOCTOR_SCHEDULE_CREATE_DTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(responseDTO.id()));
    }

    @Test
    @DisplayName("PUT /api/v1/doctor-schedules/{id} - should update schedule and return it")
    void update_ShouldReturnUpdatedSchedule() throws Exception {
        Long id = 1L;
        DoctorScheduleResponseDTO responseDTO = DOCTOR_SCHEDULE_RESPONSE_DTO;

        when(doctorScheduleService.update(eq(id), any(DoctorScheduleUpdateDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/v1/doctor-schedules/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(DOCTOR_SCHEDULE_UPDATE_DTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDTO.id()));
    }

    @Test
    @DisplayName("DELETE /api/v1/doctor-schedules/{id} - should delete schedule and return no content")
    void delete_ShouldReturnNoContent() throws Exception {
        Long id = 1L;
        doNothing().when(doctorScheduleService).delete(id);

        mockMvc.perform(delete("/api/v1/doctor-schedules/{id}", id))
                .andExpect(status().isNoContent());
    }
}
