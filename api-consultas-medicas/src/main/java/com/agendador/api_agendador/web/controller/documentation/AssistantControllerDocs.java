package com.agendador.api_agendador.web.controller.documentation;

import com.agendador.api_agendador.web.dto.assistant.AssistantCreateDTO;
import com.agendador.api_agendador.web.dto.assistant.AssistantResponseDTO;
import com.agendador.api_agendador.web.dto.assistant.AssistantUpdateDTO;
import com.agendador.api_agendador.web.dto.common.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Assistants Controller", description = "Endpoints for assistant management")
public interface AssistantControllerDocs {

    @Operation(summary = "Find assistant by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Assistant found"),
            @ApiResponse(responseCode = "404", description = "Assistant not found")
    })
    @GetMapping("/{id}")
    ResponseEntity<AssistantResponseDTO> findById(@PathVariable Long id);

    @Operation(summary = "Find all assistants")
    @GetMapping
    ResponseEntity<PageResponse<AssistantResponseDTO>> findAll(Pageable pageable);

    @Operation(summary = "Find assistant by registration number", responses = {
            @ApiResponse(responseCode = "200", description = "Assistant found"),
            @ApiResponse(responseCode = "404", description = "Assistant not found")
    })
    @GetMapping("/by-registration-number")
    ResponseEntity<AssistantResponseDTO> findByRegistrationNumber(@RequestParam String registrationNumber);

    @Operation(summary = "Create a new assistant")
    @PostMapping
    ResponseEntity<AssistantResponseDTO> create(@Valid @RequestBody AssistantCreateDTO dto, Authentication authentication);

    @Operation(summary = "Update assistant data")
    @PatchMapping("/{id}")
    ResponseEntity<AssistantResponseDTO> update(@PathVariable Long id, @Valid @RequestBody AssistantUpdateDTO dto);
}