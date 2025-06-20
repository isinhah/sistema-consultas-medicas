package com.agendador.api_agendador.web.controller;

import com.agendador.api_agendador.service.PatientService;
import com.agendador.api_agendador.web.dto.PatientCreateDTO;
import com.agendador.api_agendador.web.dto.PatientResponseDTO;
import com.agendador.api_agendador.web.dto.PatientUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> findById(@PathVariable Long id) {
        PatientResponseDTO dto = patientService.findById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<PatientResponseDTO>> findAll(Pageable pageable) {
        Page<PatientResponseDTO> page = patientService.findAll(pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/by-cpf")
    public ResponseEntity<PatientResponseDTO> findByCpf(@RequestParam String cpf) {
        PatientResponseDTO dto = patientService.findByCpf(cpf);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PatientResponseDTO> create(@RequestBody PatientCreateDTO dto) {
        PatientResponseDTO patient = patientService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(patient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> update(@PathVariable Long id, @RequestBody PatientUpdateDTO dto) {
        PatientResponseDTO patient = patientService.update(id, dto);
        return new ResponseEntity<>(patient, HttpStatus.OK);
    }
}