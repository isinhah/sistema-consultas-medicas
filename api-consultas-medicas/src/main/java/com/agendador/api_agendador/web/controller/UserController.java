package com.agendador.api_agendador.web.controller;

import com.agendador.api_agendador.service.UserService;
import com.agendador.api_agendador.web.dto.UserCreateDTO;
import com.agendador.api_agendador.web.dto.UserResponseDTO;
import com.agendador.api_agendador.web.dto.UserUpdateDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        UserResponseDTO dto = userService.findById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> findAll(Pageable pageable) {
        Page<UserResponseDTO> page = userService.findAll(pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<UserResponseDTO>> findByName(@RequestParam String name, Pageable pageable) {
        Page<UserResponseDTO> page = userService.findByName(name, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/role")
    public ResponseEntity<Page<UserResponseDTO>> findByRole(@RequestParam String role, Pageable pageable) {
        Page<UserResponseDTO> page = userService.findByRole(role, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/by-contact")
    public ResponseEntity<UserResponseDTO> findByEmailOrPhone(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone
    ) {
        UserResponseDTO dto = userService.findByEmailOrPhone(email, phone);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody UserCreateDTO dto) {
        UserResponseDTO user = userService.create(dto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO dto) {
        UserResponseDTO user = userService.update(id, dto);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}