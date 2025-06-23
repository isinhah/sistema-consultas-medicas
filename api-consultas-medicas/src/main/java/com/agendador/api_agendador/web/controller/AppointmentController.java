package com.agendador.api_agendador.web.controller;

import com.agendador.api_agendador.entity.Assistant;
import com.agendador.api_agendador.entity.enums.AppointmentStatus;
import com.agendador.api_agendador.repository.AssistantRepository;
import com.agendador.api_agendador.service.AppointmentService;
import com.agendador.api_agendador.web.dto.appointment.AppointmentBookDTO;
import com.agendador.api_agendador.web.dto.appointment.AppointmentCreateSlotDTO;
import com.agendador.api_agendador.web.dto.appointment.AppointmentResponseDTO;
import com.agendador.api_agendador.web.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final AssistantRepository assistantRepository;

    public AppointmentController(AppointmentService appointmentService, AssistantRepository assistantRepository) {
        this.appointmentService = appointmentService;
        this.assistantRepository = assistantRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> findById(@PathVariable UUID id) {
        AppointmentResponseDTO dto = appointmentService.findById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<AppointmentResponseDTO>> searchAppointments(
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
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/specialties/{specialtyId}/available")
    public ResponseEntity<Page<AppointmentResponseDTO>> findAvailableBySpecialty(
            @PathVariable Long specialtyId,
            Pageable pageable
    ) {
        Page<AppointmentResponseDTO> page = appointmentService.findAvailableAppointmentsBySpecialty(specialtyId, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @PostMapping("/slots")
    public AppointmentResponseDTO createSlot(
            @RequestBody @Valid AppointmentCreateSlotDTO dto,
            @RequestParam Long assistantId
    ) {
        Assistant assistant = assistantRepository.findById(assistantId)
                .orElseThrow(() -> new ResourceNotFoundException("Assistant not found with id: " + assistantId));

        return appointmentService.createSlot(dto, assistant);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AppointmentResponseDTO> updateStatus(
            @PathVariable UUID id,
            @RequestParam AppointmentStatus status
    ) {
        AppointmentResponseDTO dto = appointmentService.updateStatus(id, status);
        return new ResponseEntity<>(dto, HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/book")
    public AppointmentResponseDTO bookAppointment(
            @PathVariable UUID id,
            @RequestParam Long patientId
    ) {
        AppointmentBookDTO dto = new AppointmentBookDTO(id);
        return appointmentService.bookAppointment(dto, patientId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        appointmentService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}