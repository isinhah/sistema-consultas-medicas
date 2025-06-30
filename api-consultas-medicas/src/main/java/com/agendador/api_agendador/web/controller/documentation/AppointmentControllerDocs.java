package com.agendador.api_agendador.web.controller.documentation;

import com.agendador.api_agendador.entity.enums.AppointmentStatus;
import com.agendador.api_agendador.web.dto.appointment.AppointmentBookDTO;
import com.agendador.api_agendador.web.dto.appointment.AppointmentCreateSlotDTO;
import com.agendador.api_agendador.web.dto.appointment.AppointmentResponseDTO;
import com.agendador.api_agendador.web.dto.common.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Tag(name = "Appointments Controller", description = "Endpoints for managing appointments")
public interface AppointmentControllerDocs {

    @Operation(summary = "Find appointment by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Appointment found"),
            @ApiResponse(responseCode = "404", description = "Appointment not found")
    })
    @GetMapping("/{id}")
    ResponseEntity<AppointmentResponseDTO> findById(@PathVariable UUID id);

    @Operation(summary = "Search appointments by filters")
    @GetMapping
    ResponseEntity<PageResponse<AppointmentResponseDTO>> searchAppointments(
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) Long assistantId,
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) AppointmentStatus status,
            @RequestParam(required = false) LocalDateTime startDateTime,
            @RequestParam(required = false) LocalDateTime endDateTime,
            Pageable pageable
    );

    @Operation(summary = "Find available appointments by specialty")
    @GetMapping("/specialties/{specialtyId}/available")
    ResponseEntity<PageResponse<AppointmentResponseDTO>> findAvailableBySpecialty(
            @PathVariable Long specialtyId,
            Pageable pageable
    );

    @Operation(summary = "Create an available slot for appointment")
    @PostMapping("/slots")
    AppointmentResponseDTO createSlot(
            @Valid @RequestBody AppointmentCreateSlotDTO dto,
            Authentication authentication
    );

    @Operation(summary = "Update appointment status")
    @PatchMapping("/{id}/status")
    ResponseEntity<AppointmentResponseDTO> updateStatus(
            @PathVariable UUID id,
            @RequestParam AppointmentStatus status
    );

    @Operation(summary = "Book an appointment")
    @PostMapping("/{id}/book")
    AppointmentResponseDTO bookAppointment(
            @PathVariable UUID id,
            Authentication authentication
    );

    @Operation(summary = "Delete an appointment")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable UUID id);
}