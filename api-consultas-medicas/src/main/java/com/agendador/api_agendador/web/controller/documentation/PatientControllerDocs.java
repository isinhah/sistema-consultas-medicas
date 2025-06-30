package com.agendador.api_agendador.web.controller.documentation;

import com.agendador.api_agendador.web.dto.common.PageResponse;
import com.agendador.api_agendador.web.dto.patient.PatientCreateDTO;
import com.agendador.api_agendador.web.dto.patient.PatientResponseDTO;
import com.agendador.api_agendador.web.dto.patient.PatientUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Patients Controller", description = "Endpoints for patient management")
public interface PatientControllerDocs {

    @Operation(summary = "Find patient by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Patient found"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @GetMapping("/{id}")
    ResponseEntity<PatientResponseDTO> findById(@PathVariable Long id);

    @Operation(summary = "Find all patients")
    @GetMapping
    ResponseEntity<PageResponse<PatientResponseDTO>> findAll(Pageable pageable);

    @Operation(summary = "Find patient by CPF", responses = {
            @ApiResponse(responseCode = "200", description = "Patient found"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @GetMapping("/by-cpf")
    ResponseEntity<PatientResponseDTO> findByCpf(@RequestParam String cpf);

    @Operation(summary = "Create a new patient")
    @PostMapping
    ResponseEntity<PatientResponseDTO> create(@Valid @RequestBody PatientCreateDTO dto, Authentication authentication);

    @Operation(summary = "Update patient data")
    @PatchMapping("/{id}")
    ResponseEntity<PatientResponseDTO> update(@PathVariable Long id, @Valid @RequestBody PatientUpdateDTO dto);
}