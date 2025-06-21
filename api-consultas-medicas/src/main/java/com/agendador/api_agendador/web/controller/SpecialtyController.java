package com.agendador.api_agendador.web.controller;

import com.agendador.api_agendador.service.SpecialtyService;
import com.agendador.api_agendador.web.dto.SpecialtyCreateDTO;
import com.agendador.api_agendador.web.dto.SpecialtyResponseDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/specialties")
public class SpecialtyController {

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
    public ResponseEntity<Page<SpecialtyResponseDTO>> findAll(Pageable pageable) {
        Page<SpecialtyResponseDTO> page = specialtyService.findAll(pageable);
        return new ResponseEntity<>(page,  HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<SpecialtyResponseDTO> findByName(@RequestParam String name) {
        SpecialtyResponseDTO dto = specialtyService.findByName(name);
        return new ResponseEntity<>(dto,  HttpStatus.OK);
    }

    @GetMapping("/by-doctor-id/{doctorId}")
    public ResponseEntity<Page<SpecialtyResponseDTO>> findByDoctorId(@PathVariable("doctorId") Long doctorId, Pageable pageable) {
        Page<SpecialtyResponseDTO> page = specialtyService.findByDoctorId(doctorId, pageable);
        return new ResponseEntity<>(page,  HttpStatus.OK);
    }

    @GetMapping("/by-doctor-crm")
    public ResponseEntity<Page<SpecialtyResponseDTO>> findByDoctorCrm(@RequestParam String crm, Pageable pageable) {
        Page<SpecialtyResponseDTO> page = specialtyService.findByDoctorCrm(crm, pageable);
        return new ResponseEntity<>(page,  HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SpecialtyResponseDTO> create(@Valid @RequestBody SpecialtyCreateDTO dto) {
        SpecialtyResponseDTO specialty = specialtyService.create(dto);
        return new ResponseEntity<>(specialty,  HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<Void> delete(@PathVariable Long id) {
        specialtyService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}