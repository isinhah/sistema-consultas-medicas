package com.agendador.api_agendador.service;

import com.agendador.api_agendador.entity.User;
import com.agendador.api_agendador.entity.enums.Role;
import com.agendador.api_agendador.repository.UserRepository;
import com.agendador.api_agendador.security.JwtTokenService;
import com.agendador.api_agendador.web.dto.authentication.LoginRequestDTO;
import com.agendador.api_agendador.web.dto.authentication.LoginResponseDTO;
import com.agendador.api_agendador.web.dto.authentication.RegisterResponseDTO;
import com.agendador.api_agendador.web.dto.user.UserCreateDTO;
import com.agendador.api_agendador.web.exception.InvalidPasswordException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class AuthenticationService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtTokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UserService userService, UserRepository userRepository, JwtTokenService tokenService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    public RegisterResponseDTO register(UserCreateDTO dto) {
        User createdUser = userService.create(dto);

        String token = tokenService.generateToken(createdUser);
        Instant expiresAt = tokenService.generateExpirationDate();

        return new RegisterResponseDTO(
                createdUser.getId(),
                "Bearer",
                token,
                expiresAt
        );
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        User user = userService.findByEmail(dto.email());

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new InvalidPasswordException("Invalid password");
        }

        String token = tokenService.generateToken(user);
        Instant expiresAt = tokenService.generateExpirationDate();

        return new LoginResponseDTO(
                user.getId(),
                "Bearer",
                token,
                user.getRole().name(),
                expiresAt
        );
    }

    @Transactional
    public void promoteToAdmin(Long userId) {
        User user = userService.findEntityById(userId);

        if (user.getRole() != Role.ADMIN) {
            user.setRole(Role.ADMIN);
            userRepository.save(user);
        }
    }
}