package com.agendador.api_agendador.web.controller.documentation;

import com.agendador.api_agendador.entity.enums.DayOfWeek;
import com.agendador.api_agendador.web.dto.common.PageResponse;
import com.agendador.api_agendador.web.dto.doctor_schedule.DoctorScheduleCreateDTO;
import com.agendador.api_agendador.web.dto.doctor_schedule.DoctorScheduleResponseDTO;
import com.agendador.api_agendador.web.dto.doctor_schedule.DoctorScheduleUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;

@Tag(name = "Doctor Schedules Controller", description = "Endpoints for managing doctor schedules")
public interface DoctorScheduleControllerDocs {

    @Operation(summary = "Find schedule by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Schedule found"),
            @ApiResponse(responseCode = "404", description = "Schedule not found")
    })
    @GetMapping("/{id}")
    ResponseEntity<DoctorScheduleResponseDTO> findById(@PathVariable Long id);

    @Operation(summary = "Search schedules by doctor")
    @GetMapping("/search")
    ResponseEntity<PageResponse<DoctorScheduleResponseDTO>> searchSchedules(
            @RequestParam Long doctorId,
            @RequestParam(required = false) DayOfWeek dayOfWeek,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime,
            Pageable pageable
    );

    @Operation(summary = "Find schedules by specialty")
    @GetMapping("/specialty/{specialtyId}")
    ResponseEntity<PageResponse<DoctorScheduleResponseDTO>> findSchedulesBySpecialty(
            @PathVariable Long specialtyId,
            Pageable pageable
    );

    @Operation(summary = "Create a new schedule")
    @PostMapping
    ResponseEntity<DoctorScheduleResponseDTO> create(@Valid @RequestBody DoctorScheduleCreateDTO dto);

    @Operation(summary = "Update a schedule")
    @PutMapping("/{id}")
    ResponseEntity<DoctorScheduleResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody DoctorScheduleUpdateDTO dto
    );

    @Operation(summary = "Delete a schedule")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);
}