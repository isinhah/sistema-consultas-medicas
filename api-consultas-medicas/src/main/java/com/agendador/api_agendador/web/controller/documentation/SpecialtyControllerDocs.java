package com.agendador.api_agendador.web.controller.documentation;

import com.agendador.api_agendador.web.dto.common.PageResponse;
import com.agendador.api_agendador.web.dto.specialty.SpecialtyCreateDTO;
import com.agendador.api_agendador.web.dto.specialty.SpecialtyResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Specialties Controller", description = "Endpoints for managing medical specialties")
public interface SpecialtyControllerDocs {

    @Operation(summary = "Find specialty by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Specialty found"),
            @ApiResponse(responseCode = "404", description = "Specialty not found")
    })
    @GetMapping("/{id}")
    ResponseEntity<SpecialtyResponseDTO> findById(@PathVariable Long id);

    @Operation(summary = "Find all specialties")
    @GetMapping
    ResponseEntity<PageResponse<SpecialtyResponseDTO>> findAll(Pageable pageable);

    @Operation(summary = "Find specialty by name", responses = {
            @ApiResponse(responseCode = "200", description = "Specialty found"),
            @ApiResponse(responseCode = "404", description = "Specialty not found")
    })
    @GetMapping("/search")
    ResponseEntity<SpecialtyResponseDTO> findByName(@RequestParam String name);

    @Operation(summary = "Find specialties by doctor ID")
    @GetMapping("/by-doctor-id/{doctorId}")
    ResponseEntity<PageResponse<SpecialtyResponseDTO>> findByDoctorId(@PathVariable("doctorId") Long doctorId, Pageable pageable);

    @Operation(summary = "Find specialties by doctor CRM")
    @GetMapping("/by-doctor-crm")
    ResponseEntity<PageResponse<SpecialtyResponseDTO>> findByDoctorCrm(@RequestParam String crm, Pageable pageable);

    @Operation(summary = "Create a new specialty")
    @PostMapping
    ResponseEntity<SpecialtyResponseDTO> create(@Valid @RequestBody SpecialtyCreateDTO dto);

    @Operation(summary = "Delete a specialty by ID")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id);
}