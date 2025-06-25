package com.agendador.api_agendador.web.controller;

import com.agendador.api_agendador.security.CustomUserDetails;
import com.agendador.api_agendador.service.AssistantService;
import com.agendador.api_agendador.web.dto.assistant.AssistantCreateDTO;
import com.agendador.api_agendador.web.dto.assistant.AssistantResponseDTO;
import com.agendador.api_agendador.web.dto.assistant.AssistantUpdateDTO;
import com.agendador.api_agendador.web.dto.common.PageResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/assistants")
public class AssistantController {

    private final AssistantService assistantService;

    public AssistantController(AssistantService assistantService) {
        this.assistantService = assistantService;
    }

    @PreAuthorize("#id == principal.id or hasAnyRole('ADMIN', 'ASSISTANT', 'DOCTOR')")
    @GetMapping("/{id}")
    public ResponseEntity<AssistantResponseDTO> findById(@PathVariable Long id) {
        AssistantResponseDTO dto = assistantService.findById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ASSISTANT', 'DOCTOR')")
    @GetMapping
    public ResponseEntity<PageResponse<AssistantResponseDTO>> findAll(Pageable pageable) {
        Page<AssistantResponseDTO> page = assistantService.findAll(pageable);
        return ResponseEntity.ok(new PageResponse<>(page));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ASSISTANT', 'DOCTOR')")
    @GetMapping("/by-registration-number")
    public ResponseEntity<AssistantResponseDTO> findByRegistrationNumber(@RequestParam String registrationNumber) {
        AssistantResponseDTO dto = assistantService.findByRegistrationNumber(registrationNumber);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<AssistantResponseDTO> create(
            @RequestBody @Valid AssistantCreateDTO dto,
            Authentication authentication
    ) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();

        AssistantResponseDTO assistant = assistantService.create(dto, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(assistant);
    }

    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<AssistantResponseDTO> update(@PathVariable Long id, @Valid @RequestBody AssistantUpdateDTO dto) {
        AssistantResponseDTO assistant = assistantService.update(id, dto);
        return new ResponseEntity<>(assistant, HttpStatus.OK);
    }
}