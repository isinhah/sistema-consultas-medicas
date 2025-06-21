package com.agendador.api_agendador.web.controller;

import com.agendador.api_agendador.service.AssistantService;
import com.agendador.api_agendador.web.dto.AssistantCreateDTO;
import com.agendador.api_agendador.web.dto.AssistantResponseDTO;
import com.agendador.api_agendador.web.dto.AssistantUpdateDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/assistants")
public class AssistantController {

    private final AssistantService assistantService;

    public AssistantController(AssistantService assistantService) {
        this.assistantService = assistantService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssistantResponseDTO> findById(@PathVariable Long id) {
        AssistantResponseDTO dto = assistantService.findById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<AssistantResponseDTO>> findAll(Pageable pageable) {
        Page<AssistantResponseDTO> page = assistantService.findAll(pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/by-registration-number")
    public ResponseEntity<AssistantResponseDTO> findByRegistrationNumber(@RequestParam String registrationNumber) {
        AssistantResponseDTO dto = assistantService.findByRegistrationNumber(registrationNumber);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AssistantResponseDTO> create(@Valid @RequestBody AssistantCreateDTO dto) {
        AssistantResponseDTO assistant = assistantService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(assistant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssistantResponseDTO> update(@PathVariable Long id, @Valid @RequestBody AssistantUpdateDTO dto) {
        AssistantResponseDTO assistant = assistantService.update(id, dto);
        return new ResponseEntity<>(assistant, HttpStatus.OK);
    }
}