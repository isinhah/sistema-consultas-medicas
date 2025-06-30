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
            @ApiResponse(responseCode = "404", description = "Doctor not found")
    })
    @GetMapping("/{id}")
    ResponseEntity<DoctorResponseDTO> findById(@PathVariable Long id);

    @Operation(summary = "Find all doctors")
    @GetMapping
    ResponseEntity<PageResponse<DoctorResponseDTO>> findAll(Pageable pageable);

    @Operation(summary = "Find doctor by CRM", responses = {
            @ApiResponse(responseCode = "200", description = "Doctor found"),
            @ApiResponse(responseCode = "404", description = "Doctor not found")
    })
    @GetMapping("/by-crm")
    ResponseEntity<DoctorResponseDTO> findByCrm(@RequestParam String crm);

    @Operation(summary = "Create a new doctor")
    @PostMapping
    ResponseEntity<DoctorResponseDTO> create(@Valid @RequestBody DoctorCreateDTO dto, Authentication authentication);

    @Operation(summary = "Update doctor data")
    @PatchMapping("/{id}")
    ResponseEntity<DoctorResponseDTO> update(@PathVariable Long id, @Valid @RequestBody DoctorUpdateDTO dto);
}
