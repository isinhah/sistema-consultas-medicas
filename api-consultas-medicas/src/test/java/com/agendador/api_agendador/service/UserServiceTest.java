package com.agendador.api_agendador.service;

import com.agendador.api_agendador.entity.User;
import com.agendador.api_agendador.entity.enums.Role;
import com.agendador.api_agendador.repository.UserRepository;
import com.agendador.api_agendador.util.SecurityContextTestUtil;
import com.agendador.api_agendador.util.UserConstants;
import com.agendador.api_agendador.web.dto.user.PasswordUpdateDTO;
import com.agendador.api_agendador.web.dto.user.UserMapper;
import com.agendador.api_agendador.web.dto.user.UserResponseDTO;
import com.agendador.api_agendador.web.exception.BadRequestException;
import com.agendador.api_agendador.web.exception.InvalidPasswordException;
import com.agendador.api_agendador.web.exception.ResourceAlreadyExistsException;
import com.agendador.api_agendador.web.exception.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static com.agendador.api_agendador.util.AuthConstants.USER_CREATE_DTO;
import static com.agendador.api_agendador.util.UserConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setupSecurityContext() {
        SecurityContextTestUtil.mockAuthenticatedUser(USER_ID, USER_EMAIL, Role.USER);
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextTestUtil.clear();
    }

    @Test
    void findById_ShouldReturnUserResponseDto_WhenSuccessful() {
        User user = UserConstants.createUserEntity();
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        UserResponseDTO response = userService.findById(USER_ID);

        assertThat(response).usingRecursiveComparison().isEqualTo(USER_RESPONSE_DTO);
        verify(userRepository).findById(USER_ID);
    }

    @Test
    void findById_ShouldThrowResourceNotFoundException_WhenUserNotFound() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findById(USER_ID));
    }

    @Test
    void findAll_ShouldReturnPageOfUserResponseDto_WhenSuccessful() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(List.of(USER_ENTITY), pageable, 1);

        when(userRepository.findAll(pageable)).thenReturn(userPage);

        Page<UserResponseDTO> result = userService.findAll(pageable);

        assertThat(result.getContent())
                .hasSize(1)
                .contains(USER_RESPONSE_DTO);

        verify(userRepository).findAll(pageable);
    }

    @Test
    void findByName_ShouldReturnPageOfUserResponseDTO_WhenSuccessful() {
        PageRequest pageable = PageRequest.of(0, 10);
        User user = UserConstants.createUserEntity();

        when(userRepository.findByNameIgnoreCase(USER_NAME, pageable))
                .thenReturn(new PageImpl<>(List.of(user), pageable, 1));

        Page<UserResponseDTO> result = userService.findByName(USER_NAME, pageable);

        assertThat(result.getContent())
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparator()
                .contains(USER_RESPONSE_DTO);

        verify(userRepository).findByNameIgnoreCase(USER_NAME, pageable);
    }

    @Test
    void findByName_ShouldThrowBadRequestException_WhenNameIsNull() {
        PageRequest pageable = PageRequest.of(0, 10);

        assertThrows(BadRequestException.class, () -> userService.findByName(null, pageable));
    }

    @Test
    void findByName_ShouldThrowBadRequestException_WhenNameIsBlank() {
        PageRequest pageable = PageRequest.of(0, 10);

        assertThrows(BadRequestException.class, () -> userService.findByName("  ", pageable));
    }

    @Test
    void findByRole_ShouldReturnPageOfUserResponseDTO_WhenSuccessful() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(List.of(USER_ENTITY), pageable, 1);

        when(userRepository.findByRole(USER_ROLE, pageable)).thenReturn(userPage);

        Page<UserResponseDTO> result = userService.findByRole(USER_ROLE.name(), pageable);

        assertThat(result.getContent())
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparator()
                .contains(USER_RESPONSE_DTO);

        verify(userRepository).findByRole(USER_ROLE, pageable);
    }

    @Test
    void findByRole_ShouldThrowBadRequestException_WhenRoleIsNull() {
        PageRequest pageable = PageRequest.of(0, 10);

        assertThrows(BadRequestException.class, () -> userService.findByRole(null, pageable));
    }

    @Test
    void findByRole_ShouldThrowBadRequestException_WhenRoleIsBlank() {
        PageRequest pageable = PageRequest.of(0, 10);

        assertThrows(BadRequestException.class, () -> userService.findByRole("   ", pageable));
    }

    @Test
    void findByEmailOrPhone_ShouldReturnUserResponseDTO_WhenSuccessful() {
        when(userRepository.findByEmailOrPhone(USER_EMAIL, USER_PHONE))
                .thenReturn(Optional.of(USER_ENTITY));

        UserResponseDTO response = userService.findByEmailOrPhone(USER_EMAIL, USER_PHONE);

        assertThat(response)
                .usingRecursiveComparison()
                .isEqualTo(USER_RESPONSE_DTO);

        verify(userRepository).findByEmailOrPhone(USER_EMAIL, USER_PHONE);
    }

    @Test
    void findByEmailOrPhone_ShouldThrowBadRequestException_WhenBothEmailAndPhoneAreEmpty() {
        assertThrows(BadRequestException.class, () -> userService.findByEmailOrPhone("", ""));
    }

    @Test
    void findByEmailOrPhone_ShouldThrowResourceNotFoundException_WhenUserNotFound() {
        when(userRepository.findByEmailOrPhone(USER_EMAIL, USER_PHONE))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findByEmailOrPhone(USER_EMAIL, USER_PHONE));
    }

    @Test
    void create_ShouldSaveAndReturnUser_WhenEmailOrPhoneNotExists() {
        User userToSave = UserMapper.INSTANCE.toEntity(USER_CREATE_DTO);
        userToSave.setRole(Role.USER);
        userToSave.setPassword(USER_PASSWORD_ENCODED);

        when(userRepository.existsByEmailOrPhone(USER_CREATE_DTO.email(), USER_CREATE_DTO.phone())).thenReturn(false);
        when(passwordEncoder.encode(USER_CREATE_DTO.password())).thenReturn(USER_PASSWORD_ENCODED);
        when(userRepository.save(any(User.class))).thenReturn(userToSave);

        User createdUser = userService.create(USER_CREATE_DTO);

        assertNotNull(createdUser);
        assertEquals(Role.USER, createdUser.getRole());
        assertEquals(USER_PASSWORD_ENCODED, createdUser.getPassword());

        verify(userRepository).existsByEmailOrPhone(USER_CREATE_DTO.email(), USER_CREATE_DTO.phone());
        verify(passwordEncoder).encode(USER_CREATE_DTO.password());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void create_ShouldThrowResourceAlreadyExistsException_WhenEmailOrPhoneExists() {
        when(userRepository.existsByEmailOrPhone(USER_CREATE_DTO.email(), USER_CREATE_DTO.phone())).thenReturn(true);

        ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class, () ->
                userService.create(USER_CREATE_DTO)
        );

        assertEquals("Email or phone already in use", exception.getMessage());

        verify(userRepository).existsByEmailOrPhone(USER_CREATE_DTO.email(), USER_CREATE_DTO.phone());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any());
    }

    @Test
    void update_ShouldReturnUserResponseDto_WhenSuccessful() {
        User user = UserConstants.createUserEntity();

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmailAndIdNot(USER_UPDATE_DTO.email(), USER_ID)).thenReturn(false);
        when(userRepository.existsByPhoneAndIdNot(USER_UPDATE_DTO.phone(), USER_ID)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponseDTO response = userService.update(USER_ID, USER_UPDATE_DTO);

        assertNotNull(response);
        assertEquals(USER_UPDATE_DTO.name(), response.name());
        assertEquals(USER_UPDATE_DTO.email(), response.email());
        assertEquals(USER_UPDATE_DTO.phone(), response.phone());
        assertEquals(USER_UPDATE_DTO.birthDate(), response.birthDate());

        verify(userRepository).findById(USER_ID);
        verify(userRepository).existsByEmailAndIdNot(USER_UPDATE_DTO.email(), USER_ID);
        verify(userRepository).existsByPhoneAndIdNot(USER_UPDATE_DTO.phone(), USER_ID);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void update_ShouldThrowResourceNotFoundException_WhenUserNotFound() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.update(USER_ID, USER_UPDATE_DTO);
        });

        verify(userRepository).findById(USER_ID);
    }

    @Test
    void update_ShouldThrowResourceAlreadyExistsException_WhenEmailAlreadyInUse() {
        User user = UserConstants.createUserEntity();
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        when(userRepository.existsByEmailAndIdNot(USER_UPDATE_DTO.email(), USER_ID)).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            userService.update(USER_ID, USER_UPDATE_DTO);
        });

        verify(userRepository).findById(USER_ID);
        verify(userRepository).existsByEmailAndIdNot(USER_UPDATE_DTO.email(), USER_ID);
    }

    @Test
    void update_ShouldThrowResourceAlreadyExistsException_WhenPhoneAlreadyInUse() {
        User user = UserConstants.createUserEntity();
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        when(userRepository.existsByEmailAndIdNot(USER_UPDATE_DTO.email(), USER_ID)).thenReturn(false);
        when(userRepository.existsByPhoneAndIdNot(USER_UPDATE_DTO.phone(), USER_ID)).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            userService.update(USER_ID, USER_UPDATE_DTO);
        });

        verify(userRepository).findById(USER_ID);
        verify(userRepository).existsByEmailAndIdNot(USER_UPDATE_DTO.email(), USER_ID);
        verify(userRepository).existsByPhoneAndIdNot(USER_UPDATE_DTO.phone(), USER_ID);
    }

    @Test
    void updatePassword_ShouldUpdatePassword_WhenCurrentPasswordIsCorrect() {
        User user = USER_ENTITY;
        PasswordUpdateDTO dto = PASSWORD_UPDATE_DTO;

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(eq(dto.currentPassword()), anyString())).thenReturn(true);
        when(passwordEncoder.encode(dto.newPassword())).thenReturn(USER_PASSWORD_ENCODED);
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.updatePassword(USER_ID, dto);

        verify(userRepository).findById(USER_ID);
        verify(passwordEncoder).matches(eq(dto.currentPassword()), anyString());
        verify(passwordEncoder).encode(dto.newPassword());
        verify(userRepository).save(user);
    }

    @Test
    void updatePassword_ShouldThrowResourceNotFoundException_WhenUserNotFound() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            userService.updatePassword(USER_ID, PASSWORD_UPDATE_DTO);
        });

        verify(userRepository).findById(USER_ID);
    }

    @Test
    void updatePassword_ShouldThrowInvalidPasswordException_WhenCurrentPasswordIsIncorrect() {
        User user = USER_ENTITY;
        PasswordUpdateDTO dto = PASSWORD_UPDATE_DTO;

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.currentPassword(), user.getPassword())).thenReturn(false);

        org.junit.jupiter.api.Assertions.assertThrows(InvalidPasswordException.class, () -> {
            userService.updatePassword(USER_ID, dto);
        });

        verify(userRepository).findById(USER_ID);
        verify(passwordEncoder).matches(dto.currentPassword(), user.getPassword());
    }

    @Test
    void delete_ShouldDeleteUser_WhenUserExists() {
        User existingUser = USER_ENTITY;

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(existingUser));

        userService.delete(USER_ID);

        verify(userRepository).findById(USER_ID);
        verify(userRepository).delete(existingUser);
    }

    @Test
    void delete_ShouldThrowResourceNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                userService.delete(USER_ID)
        );

        assertEquals("User not found with id: " + USER_ID, exception.getMessage());

        verify(userRepository).findById(USER_ID);
        verify(userRepository, never()).delete(any());
    }
}