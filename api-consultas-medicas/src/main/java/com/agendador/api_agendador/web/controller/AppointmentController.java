package com.agendador.api_agendador.web.controller;

import com.agendador.api_agendador.entity.enums.AppointmentStatus;
import com.agendador.api_agendador.security.CustomUserDetails;
import com.agendador.api_agendador.service.AppointmentService;
import com.agendador.api_agendador.web.controller.documentation.AppointmentControllerDocs;
import com.agendador.api_agendador.web.dto.appointment.AppointmentBookDTO;
import com.agendador.api_agendador.web.dto.appointment.AppointmentCreateSlotDTO;
import com.agendador.api_agendador.web.dto.appointment.AppointmentResponseDTO;
import com.agendador.api_agendador.web.dto.common.PageResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController implements AppointmentControllerDocs {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> findById(@PathVariable UUID id) {
        AppointmentResponseDTO dto = appointmentService.findById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ASSISTANT', 'DOCTOR')")
    @GetMapping
    public ResponseEntity<PageResponse<AppointmentResponseDTO>> searchAppointments(
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) Long assistantId,
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) AppointmentStatus status,
            @RequestParam(required = false) LocalDateTime startDateTime,
            @RequestParam(required = false) LocalDateTime endDateTime,
            Pageable pageable
    ) {
        Page<AppointmentResponseDTO> page = appointmentService.findAppointmentsByFilters(
                doctorId, assistantId, patientId, status, startDateTime, endDateTime, pageable
        );
        return ResponseEntity.ok(new PageResponse<>(page));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ASSISTANT', 'DOCTOR', 'PATIENT')")
    @GetMapping("/specialties/{specialtyId}/available")
    public ResponseEntity<PageResponse<AppointmentResponseDTO>> findAvailableBySpecialty(
            @PathVariable Long specialtyId,
            Pageable pageable
    ) {
        Page<AppointmentResponseDTO> page = appointmentService.findAvailableAppointmentsBySpecialty(specialtyId, pageable);
        return ResponseEntity.ok(new PageResponse<>(page));
    }

    @PreAuthorize("hasRole('ASSISTANT')")
    @PostMapping("/slots")
    public AppointmentResponseDTO createSlot(
            @RequestBody @Valid AppointmentCreateSlotDTO dto,
            Authentication authentication
    ) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long assistantId = userDetails.id();

        return appointmentService.createSlot(dto, assistantId);
    }

    @PreAuthorize("hasAnyRole('ASSISTANT', 'ADMIN')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<AppointmentResponseDTO> updateStatus(
            @PathVariable UUID id,
            @RequestParam AppointmentStatus status
    ) {
        AppointmentResponseDTO dto = appointmentService.updateStatus(id, status);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping("/{id}/book")
    public AppointmentResponseDTO bookAppointment(
            @PathVariable UUID id,
            Authentication authentication
    ) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long patientId = userDetails.id();

        AppointmentBookDTO dto = new AppointmentBookDTO(id);
        return appointmentService.bookAppointment(dto, patientId);
    }

    @PreAuthorize("hasAnyRole('ASSISTANT', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        appointmentService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}