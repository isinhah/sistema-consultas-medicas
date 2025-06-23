package com.agendador.api_agendador.web.controller;

import com.agendador.api_agendador.service.UserService;
import com.agendador.api_agendador.web.dto.user.PasswordUpdateDTO;
import com.agendador.api_agendador.web.dto.user.UserResponseDTO;
import com.agendador.api_agendador.web.dto.user.UserUpdateDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(value = "/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN') or #id == principal")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        UserResponseDTO dto = userService.findById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> findAll(Pageable pageable) {
        Page<UserResponseDTO> page = userService.findAll(pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<Page<UserResponseDTO>> findByName(@RequestParam String name, Pageable pageable) {
        Page<UserResponseDTO> page = userService.findByName(name, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/role")
    public ResponseEntity<Page<UserResponseDTO>> findByRole(@RequestParam String role, Pageable pageable) {
        Page<UserResponseDTO> page = userService.findByRole(role, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/by-contact")
    public ResponseEntity<UserResponseDTO> findByEmailOrPhone(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone
    ) {
        UserResponseDTO dto = userService.findByEmailOrPhone(email, phone);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or #id == principal")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO dto) {
        UserResponseDTO user = userService.update(id, dto);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or #userId == principal")
    @PutMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePassword(@RequestBody @Valid PasswordUpdateDTO dto, Principal principal) {
        Long userId = userService.findByEmail(principal.getName()).getId();
        userService.updatePassword(userId, dto);
    }

    @PreAuthorize("#id == principal")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}