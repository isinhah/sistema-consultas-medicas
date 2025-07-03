package com.agendador.api_agendador.web.controller.documentation;

import com.agendador.api_agendador.web.dto.common.PageResponse;
import com.agendador.api_agendador.web.dto.doctor.DoctorCreateDTO;
import com.agendador.api_agendador.web.dto.doctor.DoctorResponseDTO;
import com.agendador.api_agendador.web.dto.doctor.DoctorUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Doctors Controller", description = "Endpoints for doctor management")
public interface DoctorControllerDocs {

    @Operation(summary = "Find doctor by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Doctor found"),
            @ApiResponse(responseCode = "404", description = "Doctor not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/{id}")
    ResponseEntity<DoctorResponseDTO> findById(@PathVariable Long id);

    @Operation(summary = "Find all doctors", responses = {
            @ApiResponse(responseCode = "200", description = "Doctors found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping
    ResponseEntity<PageResponse<DoctorResponseDTO>> findAll(Pageable pageable);

    @Operation(summary = "Find doctor by CRM", responses = {
            @ApiResponse(responseCode = "200", description = "Doctor found"),
            @ApiResponse(responseCode = "404", description = "Doctor not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/by-crm")
    ResponseEntity<DoctorResponseDTO> findByCrm(@RequestParam String crm);

    @Operation(summary = "Create a new doctor", responses = {
            @ApiResponse(responseCode = "201", description = "Doctor successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid doctor data"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping
    ResponseEntity<DoctorResponseDTO> create(@Valid @RequestBody DoctorCreateDTO dto, Authentication authentication);

    @Operation(summary = "Update doctor data", responses = {
            @ApiResponse(responseCode = "200", description = "Doctor successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid update data"),
            @ApiResponse(responseCode = "404", description = "Doctor not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PatchMapping("/{id}")
    ResponseEntity<DoctorResponseDTO> update(@PathVariable Long id, @Valid @RequestBody DoctorUpdateDTO dto);
}