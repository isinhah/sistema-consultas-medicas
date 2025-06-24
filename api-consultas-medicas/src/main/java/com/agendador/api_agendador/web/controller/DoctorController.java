package com.agendador.api_agendador.web.controller;

import com.agendador.api_agendador.security.CustomUserDetails;
import com.agendador.api_agendador.service.DoctorService;
import com.agendador.api_agendador.web.dto.doctor.DoctorCreateDTO;
import com.agendador.api_agendador.web.dto.doctor.DoctorResponseDTO;
import com.agendador.api_agendador.web.dto.doctor.DoctorUpdateDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PreAuthorize("#id == principal.id or hasAnyRole('ADMIN', 'ASSISTANT', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> findById(@PathVariable Long id) {
        DoctorResponseDTO dto = doctorService.findById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ASSISTANT', 'USER')")
    @GetMapping
    public ResponseEntity<Page<DoctorResponseDTO>> findAll(Pageable pageable) {
        Page<DoctorResponseDTO> page = doctorService.findAll(pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ASSISTANT', 'USER')")
    @GetMapping("/by-crm")
    public ResponseEntity<DoctorResponseDTO> findByCrm(@RequestParam String crm) {
        DoctorResponseDTO dto = doctorService.findByCrm(crm);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<DoctorResponseDTO> create(
            @Valid @RequestBody DoctorCreateDTO dto,
            Authentication authentication
    ) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();

        DoctorResponseDTO doctor = doctorService.create(dto, userId);
        return new ResponseEntity<>(doctor, HttpStatus.CREATED);
    }

    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> update(@PathVariable Long id, @Valid @RequestBody DoctorUpdateDTO dto) {
        DoctorResponseDTO doctor = doctorService.update(id, dto);
        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }
}