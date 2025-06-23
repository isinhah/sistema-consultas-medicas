package com.agendador.api_agendador.web.controller;

import com.agendador.api_agendador.service.AuthenticationService;
import com.agendador.api_agendador.web.dto.authentication.LoginRequestDTO;
import com.agendador.api_agendador.web.dto.authentication.LoginResponseDTO;
import com.agendador.api_agendador.web.dto.authentication.RegisterResponseDTO;
import com.agendador.api_agendador.web.dto.user.UserCreateDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponseDTO register(@RequestBody @Valid UserCreateDTO dto) {
        return authenticationService.register(dto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponseDTO login(@RequestBody @Valid LoginRequestDTO dto) {
        return authenticationService.login(dto);
    }

    // @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/promote-to-admin/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void promoteToAdmin(@PathVariable Long userId) {
        authenticationService.promoteToAdmin(userId);
    }
}