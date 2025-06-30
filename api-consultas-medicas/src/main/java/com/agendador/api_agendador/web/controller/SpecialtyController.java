package com.agendador.api_agendador.web.controller;

import com.agendador.api_agendador.service.SpecialtyService;
import com.agendador.api_agendador.web.controller.documentation.SpecialtyControllerDocs;
import com.agendador.api_agendador.web.dto.common.PageResponse;
import com.agendador.api_agendador.web.dto.specialty.SpecialtyCreateDTO;
import com.agendador.api_agendador.web.dto.specialty.SpecialtyResponseDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/specialties")
public class SpecialtyController implements SpecialtyControllerDocs {

    private final SpecialtyService specialtyService;

    public SpecialtyController(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpecialtyResponseDTO> findById(@PathVariable Long id) {
        SpecialtyResponseDTO dto = specialtyService.findById(id);
        return new ResponseEntity<>(dto,  HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PageResponse<SpecialtyResponseDTO>> findAll(Pageable pageable) {
        Page<SpecialtyResponseDTO> page = specialtyService.findAll(pageable);
        return ResponseEntity.ok(new PageResponse<>(page));
    }

    @GetMapping("/search")
    public ResponseEntity<SpecialtyResponseDTO> findByName(@RequestParam String name) {
        SpecialtyResponseDTO dto = specialtyService.findByName(name);
        return new ResponseEntity<>(dto,  HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ASSISTANT')")
    @GetMapping("/by-doctor-id/{doctorId}")
    public ResponseEntity<PageResponse<SpecialtyResponseDTO>> findByDoctorId(@PathVariable("doctorId") Long doctorId, Pageable pageable) {
        Page<SpecialtyResponseDTO> page = specialtyService.findByDoctorId(doctorId, pageable);
        return ResponseEntity.ok(new PageResponse<>(page));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ASSISTANT')")
    @GetMapping("/by-doctor-crm")
    public ResponseEntity<PageResponse<SpecialtyResponseDTO>> findByDoctorCrm(@RequestParam String crm, Pageable pageable) {
        Page<SpecialtyResponseDTO> page = specialtyService.findByDoctorCrm(crm, pageable);
        return ResponseEntity.ok(new PageResponse<>(page));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ASSISTANT')")
    @PostMapping
    public ResponseEntity<SpecialtyResponseDTO> create(@Valid @RequestBody SpecialtyCreateDTO dto) {
        SpecialtyResponseDTO specialty = specialtyService.create(dto);
        return new ResponseEntity<>(specialty,  HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ASSISTANT')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        specialtyService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}