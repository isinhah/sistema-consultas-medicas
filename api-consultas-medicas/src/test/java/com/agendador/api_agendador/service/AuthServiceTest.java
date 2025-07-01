package com.agendador.api_agendador.service;

import com.agendador.api_agendador.entity.User;
import com.agendador.api_agendador.entity.enums.Role;
import com.agendador.api_agendador.repository.UserRepository;
import com.agendador.api_agendador.security.JwtTokenService;
import com.agendador.api_agendador.web.dto.auth.RegisterResponseDTO;
import com.agendador.api_agendador.web.exception.InvalidPasswordException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.agendador.api_agendador.util.AuthConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void register_ShouldReturnRegisterResponseDTO_WhenSuccessful() {
        User user = freshUserEntity();

        when(userService.create(USER_CREATE_DTO)).thenReturn(user);
        when(tokenService.generateToken(user)).thenReturn(TOKEN);
        when(tokenService.generateExpirationDate()).thenReturn(EXPIRES_AT);

        RegisterResponseDTO response = authenticationService.register(USER_CREATE_DTO);

        assertNotNull(response);
        assertEquals(user.getId(), response.userId());
        assertEquals(TOKEN_TYPE, response.tokenType());
        assertEquals(TOKEN, response.token());
        assertEquals(EXPIRES_AT, response.expiresAt());

        verify(userService).create(USER_CREATE_DTO);
        verify(tokenService).generateToken(user);
        verify(tokenService).generateExpirationDate();
    }

    @Test
    void login_ShouldReturnLoginResponseDTO_WhenCredentialsAreValid() {
        User user = freshUserEntity();

        when(userService.findByEmail(LOGIN_REQUEST_DTO.email())).thenReturn(user);
        when(passwordEncoder.matches(LOGIN_REQUEST_DTO.password(), user.getPassword())).thenReturn(true);
        when(tokenService.generateToken(user)).thenReturn(TOKEN);
        when(tokenService.generateExpirationDate()).thenReturn(EXPIRES_AT);

        var response = authenticationService.login(LOGIN_REQUEST_DTO);

        assertNotNull(response);
        assertEquals(user.getId(), response.userId());
        assertEquals(TOKEN_TYPE, response.tokenType());
        assertEquals(TOKEN, response.token());
        assertEquals(user.getRole().name(), response.role());
        assertEquals(EXPIRES_AT, response.expiresAt());

        verify(userService).findByEmail(LOGIN_REQUEST_DTO.email());
        verify(passwordEncoder).matches(LOGIN_REQUEST_DTO.password(), user.getPassword());
        verify(tokenService).generateToken(user);
    }

    @Test
    void login_ShouldThrowInvalidPasswordException_WhenPasswordDoesNotMatch() {
        User user = freshUserEntity();

        when(userService.findByEmail(LOGIN_REQUEST_DTO.email())).thenReturn(user);
        when(passwordEncoder.matches(LOGIN_REQUEST_DTO.password(), user.getPassword())).thenReturn(false);

        assertThrows(InvalidPasswordException.class, () -> authenticationService.login(LOGIN_REQUEST_DTO));

        verify(userService).findByEmail(LOGIN_REQUEST_DTO.email());
        verify(passwordEncoder).matches(LOGIN_REQUEST_DTO.password(), user.getPassword());
        verify(tokenService, never()).generateToken(any());
    }

    @Test
    void promoteToAdmin_ShouldUpdateUserRole_WhenNotAdmin() {
        User user = freshUserEntity();
        user.setRole(Role.USER);

        when(userService.findEntityById(USER_ID)).thenReturn(user);

        authenticationService.promoteToAdmin(USER_ID);

        assertEquals(Role.ADMIN, user.getRole());
        verify(userService).findEntityById(USER_ID);
        verify(userRepository).save(user);
    }

    @Test
    void promoteToAdmin_ShouldDoNothing_WhenUserIsAlreadyAdmin() {
        User user = freshUserEntity();
        user.setRole(Role.ADMIN);

        when(userService.findEntityById(USER_ID)).thenReturn(user);

        authenticationService.promoteToAdmin(USER_ID);

        assertEquals(Role.ADMIN, user.getRole());
        verify(userService).findEntityById(USER_ID);
        verify(userRepository, never()).save(any());
    }
}
