package com.agendador.api_agendador.web.controller;

import com.agendador.api_agendador.service.ReceptionistService;
import com.agendador.api_agendador.web.dto.ReceptionistCreateDTO;
import com.agendador.api_agendador.web.dto.ReceptionistResponseDTO;
import com.agendador.api_agendador.web.dto.ReceptionistUpdateDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/receptionists")
public class ReceptionistController {

    private final ReceptionistService receptionistService;

    public ReceptionistController(ReceptionistService receptionistService) {
        this.receptionistService = receptionistService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceptionistResponseDTO> findById(@PathVariable Long id) {
        ReceptionistResponseDTO dto = receptionistService.findById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<ReceptionistResponseDTO>> findAll(Pageable pageable) {
        Page<ReceptionistResponseDTO> page = receptionistService.findAll(pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/by-registration-number")
    public ResponseEntity<ReceptionistResponseDTO> findByRegistrationNumber(@RequestParam String registrationNumber) {
        ReceptionistResponseDTO dto = receptionistService.findByRegistrationNumber(registrationNumber);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ReceptionistResponseDTO> create(@Valid @RequestBody ReceptionistCreateDTO dto) {
        ReceptionistResponseDTO receptionist = receptionistService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(receptionist);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReceptionistResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ReceptionistUpdateDTO dto) {
        ReceptionistResponseDTO receptionist = receptionistService.update(id, dto);
        return new ResponseEntity<>(receptionist, HttpStatus.OK);
    }
}
