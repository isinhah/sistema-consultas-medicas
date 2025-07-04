package com.agendador.api_agendador.web.controller;

import com.agendador.api_agendador.entity.enums.DayOfWeek;
import com.agendador.api_agendador.service.DoctorScheduleService;
import com.agendador.api_agendador.web.controller.documentation.DoctorScheduleControllerDocs;
import com.agendador.api_agendador.web.dto.common.PageResponse;
import com.agendador.api_agendador.web.dto.doctor_schedule.DoctorScheduleCreateDTO;
import com.agendador.api_agendador.web.dto.doctor_schedule.DoctorScheduleResponseDTO;
import com.agendador.api_agendador.web.dto.doctor_schedule.DoctorScheduleUpdateDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;

@PreAuthorize("hasAnyRole('ADMIN', 'ASSISTANT')")
@RestController
@RequestMapping("/api/v1/doctor-schedules")
public class DoctorScheduleController implements DoctorScheduleControllerDocs {

    private final DoctorScheduleService doctorScheduleService;

    public DoctorScheduleController(DoctorScheduleService doctorScheduleService) {
        this.doctorScheduleService = doctorScheduleService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorScheduleResponseDTO> findById(@PathVariable Long id) {
        DoctorScheduleResponseDTO dto = doctorScheduleService.findById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<DoctorScheduleResponseDTO>> searchSchedules(
            @RequestParam Long doctorId,
            @RequestParam(required = false) DayOfWeek dayOfWeek,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime,
            Pageable pageable) {
        Page<DoctorScheduleResponseDTO> page = doctorScheduleService.findSchedulesByDoctorId(doctorId, dayOfWeek, startTime, endTime, pageable);
        return ResponseEntity.ok(new PageResponse<>(page));
    }

    @GetMapping("/specialty/{specialtyId}")
    public ResponseEntity<PageResponse<DoctorScheduleResponseDTO>> findSchedulesBySpecialty(
            @PathVariable Long specialtyId,
            Pageable pageable) {
        Page<DoctorScheduleResponseDTO> page = doctorScheduleService.findAvailableSchedulesBySpecialty(specialtyId, pageable);
        return ResponseEntity.ok(new PageResponse<>(page));
    }

    @PostMapping
    public ResponseEntity<DoctorScheduleResponseDTO> create(@Valid @RequestBody DoctorScheduleCreateDTO dto) {
        DoctorScheduleResponseDTO schedule = doctorScheduleService.create(dto);
        return new ResponseEntity<>(schedule, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorScheduleResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody DoctorScheduleUpdateDTO dto) {
        DoctorScheduleResponseDTO schedule = doctorScheduleService.update(id, dto);
        return new ResponseEntity<>(schedule, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        doctorScheduleService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
