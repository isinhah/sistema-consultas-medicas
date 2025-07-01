package com.agendador.api_agendador.controller;

import com.agendador.api_agendador.entity.enums.AppointmentStatus;
import com.agendador.api_agendador.security.CustomUserDetails;
import com.agendador.api_agendador.security.JwtAuthenticationFilter;
import com.agendador.api_agendador.security.JwtTokenService;
import com.agendador.api_agendador.service.AppointmentService;
import com.agendador.api_agendador.util.AppointmentConstants;
import com.agendador.api_agendador.web.controller.AppointmentController;
import com.agendador.api_agendador.web.dto.appointment.AppointmentCreateSlotDTO;
import com.agendador.api_agendador.web.dto.appointment.AppointmentResponseDTO;
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
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.agendador.api_agendador.util.AppointmentConstants.APPOINTMENT_CREATE_SLOT_DTO;
import static com.agendador.api_agendador.util.AppointmentConstants.APPOINTMENT_RESPONSE_DTO;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppointmentController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AppointmentService appointmentService;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    @DisplayName("GET /api/v1/appointments/{id} - should return appointment by id")
    void findById_ShouldReturnAppointment() throws Exception {
        UUID id = UUID.randomUUID();

        when(appointmentService.findById(id)).thenReturn(APPOINTMENT_RESPONSE_DTO);

        mockMvc.perform(get("/api/v1/appointments/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/v1/appointments - should return filtered appointments page")
    void searchAppointments_ShouldReturnPage() throws Exception {
        Page<AppointmentResponseDTO> page = new PageImpl<>(List.of(APPOINTMENT_RESPONSE_DTO));

        when(appointmentService.findAppointmentsByFilters(any(), any(), any(), any(), any(), any(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/appointments")
                        .param("doctorId", "1")
                        .param("assistantId", "2")
                        .param("patientId", "3")
                        .param("status", "BOOKED")
                        .param("startDateTime", LocalDateTime.now().minusDays(1).toString())
                        .param("endDateTime", LocalDateTime.now().plusDays(1).toString())
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/v1/appointments/specialties/{specialtyId}/available - should return available appointments by specialty")
    void findAvailableBySpecialty_ShouldReturnPage() throws Exception {
        Long specialtyId = 5L;
        Page<AppointmentResponseDTO> page = new PageImpl<>(List.of(APPOINTMENT_RESPONSE_DTO));

        when(appointmentService.findAvailableAppointmentsBySpecialty(eq(specialtyId), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/appointments/specialties/{specialtyId}/available", specialtyId)
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/v1/appointments/slots - should create appointment slot")
    void createSlot_ShouldReturnCreatedSlot() throws Exception {
        when(appointmentService.createSlot(any(AppointmentCreateSlotDTO.class), anyLong())).thenReturn(APPOINTMENT_RESPONSE_DTO);

        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.id()).thenReturn(2L);
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(userDetails);

        mockMvc.perform(post("/api/v1/appointments/slots")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(APPOINTMENT_CREATE_SLOT_DTO))
                        .principal(auth))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("PATCH /api/v1/appointments/{id}/status - should update appointment status")
    void updateStatus_ShouldReturnOk() throws Exception {
        UUID id = UUID.randomUUID();

        when(appointmentService.updateStatus(id, AppointmentStatus.BOOKED))
                .thenReturn(AppointmentConstants.APPOINTMENT_RESPONSE_DTO); // usando constante

        mockMvc.perform(patch("/api/v1/appointments/{id}/status", id)
                        .param("status", "BOOKED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(AppointmentConstants.APPOINTMENT_RESPONSE_DTO.id().toString()))
                .andExpect(jsonPath("$.status").value(AppointmentConstants.APPOINTMENT_RESPONSE_DTO.status().name()));
    }

    @Test
    @DisplayName("DELETE /api/v1/appointments/{id} - should delete appointment")
    void delete_ShouldReturnNoContent() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(appointmentService).delete(id);

        mockMvc.perform(delete("/api/v1/appointments/{id}", id))
                .andExpect(status().isNoContent());
    }
}
