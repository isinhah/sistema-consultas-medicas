package com.agendador.api_agendador.web.controller.documentation;

import com.agendador.api_agendador.web.dto.auth.LoginRequestDTO;
import com.agendador.api_agendador.web.dto.auth.LoginResponseDTO;
import com.agendador.api_agendador.web.dto.auth.RegisterResponseDTO;
import com.agendador.api_agendador.web.dto.auth.UserCreateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Authentication Controller", description = "Endpoints for user authentication and registration")
public interface AuthenticationControllerDocs {

    @Operation(summary = "Register a new user", responses = {
            @ApiResponse(responseCode = "201", description = "User successfully registered"),
            @ApiResponse(responseCode = "400", description = "Invalid user data")
    })
    @PostMapping("/register")
    RegisterResponseDTO register(@Valid @RequestBody UserCreateDTO dto);

    @Operation(summary = "User login", responses = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    LoginResponseDTO login(@Valid @RequestBody LoginRequestDTO dto);

    @Operation(summary = "Promote user to admin", responses = {
            @ApiResponse(responseCode = "204", description = "User promoted to admin successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/promote-to-admin/{userId}")
    void promoteToAdmin(@PathVariable Long userId);
}