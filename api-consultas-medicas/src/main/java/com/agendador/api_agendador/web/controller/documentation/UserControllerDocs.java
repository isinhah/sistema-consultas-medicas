package com.agendador.api_agendador.web.controller.documentation;

import com.agendador.api_agendador.web.dto.common.PageResponse;
import com.agendador.api_agendador.web.dto.user.PasswordUpdateDTO;
import com.agendador.api_agendador.web.dto.user.UserResponseDTO;
import com.agendador.api_agendador.web.dto.user.UserUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Users Controller", description = "Endpoints for user management")
public interface UserControllerDocs {

    @Operation(summary = "Find user by ID", responses = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/{id}")
    ResponseEntity<UserResponseDTO> findById(@PathVariable Long id);

    @Operation(summary = "Find all users", responses = {
            @ApiResponse(responseCode = "200", description = "Users found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping
    ResponseEntity<PageResponse<UserResponseDTO>> findAll(Pageable pageable);

    @Operation(summary = "Search users by name", responses = {
            @ApiResponse(responseCode = "200", description = "Users found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/search")
    ResponseEntity<PageResponse<UserResponseDTO>> findByName(
            @RequestParam String name,
            Pageable pageable
    );

    @Operation(summary = "Find users by role", responses = {
            @ApiResponse(responseCode = "200", description = "Users found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/role")
    ResponseEntity<PageResponse<UserResponseDTO>> findByRole(
            @RequestParam String role,
            Pageable pageable
    );

    @Operation(summary = "Find user by email or phone", responses = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/by-contact")
    ResponseEntity<UserResponseDTO> findByEmailOrPhone(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone
    );

    @Operation(summary = "Update a user", responses = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user data"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PutMapping("/{id}")
    ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO dto);

    @Operation(summary = "Update user password", responses = {
            @ApiResponse(responseCode = "204", description = "Password updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid password data"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{userId}/password")
    void updatePassword(@PathVariable Long userId, @RequestBody @Valid PasswordUpdateDTO dto);

    @Operation(summary = "Delete a user by ID", responses = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id);
}