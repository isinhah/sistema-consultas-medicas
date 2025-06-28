package com.agendador.api_agendador.web.controller;

import com.agendador.api_agendador.security.CustomUserDetails;
import com.agendador.api_agendador.service.PatientService;
import com.agendador.api_agendador.web.dto.common.PageResponse;
import com.agendador.api_agendador.web.dto.patient.PatientCreateDTO;
import com.agendador.api_agendador.web.dto.patient.PatientResponseDTO;
import com.agendador.api_agendador.web.dto.patient.PatientUpdateDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PreAuthorize("#id == principal.id or hasAnyRole('ADMIN', 'ASSISTANT', 'DOCTOR')")
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> findById(@PathVariable Long id) {
        PatientResponseDTO dto = patientService.findById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ASSISTANT', 'DOCTOR')")
    @GetMapping
    public ResponseEntity<PageResponse<PatientResponseDTO>> findAll(Pageable pageable) {
        Page<PatientResponseDTO> page = patientService.findAll(pageable);
        return ResponseEntity.ok(new PageResponse<>(page));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ASSISTANT', 'DOCTOR')")
    @GetMapping("/by-cpf")
    public ResponseEntity<PatientResponseDTO> findByCpf(@RequestParam String cpf) {
        PatientResponseDTO dto = patientService.findByCpf(cpf);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<PatientResponseDTO> create(
            @Valid @RequestBody PatientCreateDTO dto,
            Authentication authentication
    ) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.id();

        PatientResponseDTO patient = patientService.create(dto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(patient);
    }

    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> update(@PathVariable Long id, @Valid @RequestBody PatientUpdateDTO dto) {
        PatientResponseDTO patient = patientService.update(id, dto);
        return new ResponseEntity<>(patient, HttpStatus.OK);
    }
}