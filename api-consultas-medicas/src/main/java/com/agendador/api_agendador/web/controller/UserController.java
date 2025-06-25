package com.agendador.api_agendador.web.controller;

import com.agendador.api_agendador.service.UserService;
import com.agendador.api_agendador.web.dto.common.PageResponse;
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

@RestController
@RequestMapping(value = "/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        UserResponseDTO dto = userService.findById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<PageResponse<UserResponseDTO>> findAll(Pageable pageable) {
        Page<UserResponseDTO> page = userService.findAll(pageable);
        return ResponseEntity.ok(new PageResponse<>(page));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<PageResponse<UserResponseDTO>> findByName(@RequestParam String name, Pageable pageable) {
        Page<UserResponseDTO> page = userService.findByName(name, pageable);
        return ResponseEntity.ok(new PageResponse<>(page));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/role")
    public ResponseEntity<PageResponse<UserResponseDTO>> findByRole(@RequestParam String role, Pageable pageable) {
        Page<UserResponseDTO> page = userService.findByRole(role, pageable);
        return ResponseEntity.ok(new PageResponse<>(page));
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

    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO dto) {
        UserResponseDTO user = userService.update(id, dto);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PreAuthorize("authentication.principal.id == #userId or hasRole('ADMIN')")
    @PutMapping("/{userId}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePassword(@PathVariable Long userId, @RequestBody @Valid PasswordUpdateDTO dto) {
        userService.updatePassword(userId, dto);
    }

    @PreAuthorize("#id == principal.id")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}