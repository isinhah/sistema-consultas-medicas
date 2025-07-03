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
            @ApiResponse(responseCode = "404", description = "Schedule not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/{id}")
    ResponseEntity<DoctorScheduleResponseDTO> findById(@PathVariable Long id);

    @Operation(summary = "Search schedules by doctor", responses = {
            @ApiResponse(responseCode = "200", description = "Schedules found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/search")
    ResponseEntity<PageResponse<DoctorScheduleResponseDTO>> searchSchedules(
            @RequestParam Long doctorId,
            @RequestParam(required = false) DayOfWeek dayOfWeek,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime,
            Pageable pageable
    );

    @Operation(summary = "Find schedules by specialty", responses = {
            @ApiResponse(responseCode = "200", description = "Schedules found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/specialty/{specialtyId}")
    ResponseEntity<PageResponse<DoctorScheduleResponseDTO>> findSchedulesBySpecialty(
            @PathVariable Long specialtyId,
            Pageable pageable
    );

    @Operation(summary = "Create a new schedule", responses = {
            @ApiResponse(responseCode = "201", description = "Schedule created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid schedule data"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping
    ResponseEntity<DoctorScheduleResponseDTO> create(@Valid @RequestBody DoctorScheduleCreateDTO dto);

    @Operation(summary = "Update a schedule", responses = {
            @ApiResponse(responseCode = "200", description = "Schedule updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid schedule data"),
            @ApiResponse(responseCode = "404", description = "Schedule not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PutMapping("/{id}")
    ResponseEntity<DoctorScheduleResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody DoctorScheduleUpdateDTO dto
    );

    @Operation(summary = "Delete a schedule", responses = {
            @ApiResponse(responseCode = "204", description = "Schedule deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Schedule not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);
}