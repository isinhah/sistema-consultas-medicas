package com.agendador.api_agendador.service;

import com.agendador.api_agendador.entity.Assistant;
import com.agendador.api_agendador.entity.User;
import com.agendador.api_agendador.repository.AssistantRepository;
import com.agendador.api_agendador.repository.UserRepository;
import com.agendador.api_agendador.util.AssistantConstants;
import com.agendador.api_agendador.util.DoctorConstants;
import com.agendador.api_agendador.web.dto.assistant.AssistantResponseDTO;
import com.agendador.api_agendador.web.exception.BadRequestException;
import com.agendador.api_agendador.web.exception.ResourceAlreadyExistsException;
import com.agendador.api_agendador.web.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.agendador.api_agendador.util.AssistantConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AssistantServiceTest {

    @InjectMocks
    private AssistantService assistantService;

    @Mock
    private AssistantRepository assistantRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void findById_ShouldReturnAssistantResponseDTO_WhenSuccessful() {
        when(assistantRepository.findById(ASSISTANT_ID)).thenReturn(Optional.of(freshAssistantEntity()));

        AssistantResponseDTO response = assistantService.findById(ASSISTANT_ID);

        assertThat(response).usingRecursiveComparison().isEqualTo(ASSISTANT_RESPONSE_DTO);
        verify(assistantRepository).findById(ASSISTANT_ID);
    }

    @Test
    void findAll_ShouldReturnPageOfAssistantResponseDTO_WhenSuccessful() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Assistant> page = new PageImpl<>(List.of(freshAssistantEntity()), pageable, 1);

        when(assistantRepository.findAll(pageable)).thenReturn(page);

        Page<AssistantResponseDTO> responsePage = assistantService.findAll(pageable);

        assertThat(responsePage.getContent())
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparator()
                .contains(ASSISTANT_RESPONSE_DTO);
        verify(assistantRepository).findAll(pageable);
    }

    @Test
    void findByRegistrationNumber_ShouldReturnAssistantResponseDTO_WhenSuccessful() {
        when(assistantRepository.findByRegistrationNumberIgnoreCase(REGISTRATION_NUMBER))
                .thenReturn(Optional.of(freshAssistantEntity()));

        AssistantResponseDTO response = assistantService.findByRegistrationNumber(REGISTRATION_NUMBER);

        assertThat(response).usingRecursiveComparison().isEqualTo(ASSISTANT_RESPONSE_DTO);
        verify(assistantRepository).findByRegistrationNumberIgnoreCase(REGISTRATION_NUMBER);
    }

    @Test
    void findByRegistrationNumber_ShouldThrowBadRequestException_WhenRegistrationNumberIsNullOrBlank() {
        assertThrows(BadRequestException.class, () -> assistantService.findByRegistrationNumber(null));
        assertThrows(BadRequestException.class, () -> assistantService.findByRegistrationNumber(""));
        assertThrows(BadRequestException.class, () -> assistantService.findByRegistrationNumber("   "));
    }

    @Test
    void findByRegistrationNumber_ShouldThrowResourceNotFoundException_WhenAssistantNotFound() {
        String invalidRegNumber = "INVALID123";

        when(assistantRepository.findByRegistrationNumberIgnoreCase(invalidRegNumber))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> assistantService.findByRegistrationNumber(invalidRegNumber));

        assertTrue(exception.getMessage().contains(invalidRegNumber));
    }

    @Test
    void create_ShouldReturnAssistantResponseDTO_WhenSuccessful() {
        User user = freshAssistantUser();

        when(userRepository.findById(ASSISTANT_ID)).thenReturn(Optional.of(user));
        when(assistantRepository.existsByRegistrationNumber(REGISTRATION_NUMBER)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(assistantRepository.save(any(Assistant.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AssistantResponseDTO response = assistantService.create(
                ASSISTANT_CREATE_DTO, ASSISTANT_ID);

        assertNotNull(response);
        assertEquals(ASSISTANT_ID, response.id());
        assertEquals(ASSISTANT_NAME, response.name());
        assertEquals(REGISTRATION_NUMBER, response.registrationNumber());
    }

    @Test
    void create_ShouldThrowResourceNotFoundException_WhenUserNotFound() {
        when(userRepository.findById(ASSISTANT_ID)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> assistantService.create(ASSISTANT_CREATE_DTO, ASSISTANT_ID));

        assertTrue(exception.getMessage().contains("User not found with id"));
    }

    @Test
    void create_ShouldThrowResourceAlreadyExistsException_WhenUserAlreadyHasProfile() {
        User userWithDoctor = AssistantConstants.freshAssistantUser();
        userWithDoctor.setDoctor(DoctorConstants.freshDoctorEntity()); // simula que usuário já tem Doctor

        when(userRepository.findById(AssistantConstants.ASSISTANT_ID)).thenReturn(Optional.of(userWithDoctor));

        ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class,
                () -> assistantService.create(AssistantConstants.ASSISTANT_CREATE_DTO, AssistantConstants.ASSISTANT_ID));

        assertEquals("User already has a profile assigned", exception.getMessage());
    }

    @Test
    void create_ShouldThrowResourceAlreadyExistsException_WhenRegistrationNumberAlreadyInUse() {
        User user = AssistantConstants.freshAssistantUser();

        when(userRepository.findById(AssistantConstants.ASSISTANT_ID)).thenReturn(Optional.of(user));
        when(assistantRepository.existsByRegistrationNumber(AssistantConstants.REGISTRATION_NUMBER)).thenReturn(true);

        ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class,
                () -> assistantService.create(AssistantConstants.ASSISTANT_CREATE_DTO, AssistantConstants.ASSISTANT_ID));

        assertEquals("CPF already in use", exception.getMessage());
    }

    @Test
    void update_ShouldReturnUpdatedAssistantResponseDTO_WhenSuccessful() {
        Assistant existingAssistant = AssistantConstants.freshAssistantEntity();

        when(assistantRepository.findById(AssistantConstants.ASSISTANT_ID)).thenReturn(Optional.of(existingAssistant));
        when(assistantRepository.existsByRegistrationNumberAndIdNot(AssistantConstants.REGISTRATION_NUMBER, AssistantConstants.ASSISTANT_ID))
                .thenReturn(false);
        when(assistantRepository.save(any(Assistant.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AssistantResponseDTO response = assistantService.update(AssistantConstants.ASSISTANT_ID, AssistantConstants.ASSISTANT_UPDATE_DTO);

        assertNotNull(response);
        assertEquals(AssistantConstants.ASSISTANT_ID, response.id());
        assertEquals(AssistantConstants.REGISTRATION_NUMBER, response.registrationNumber());
    }

    @Test
    void update_ShouldThrowResourceNotFoundException_WhenAssistantNotFound() {
        when(assistantRepository.findById(AssistantConstants.ASSISTANT_ID)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> assistantService.update(AssistantConstants.ASSISTANT_ID, AssistantConstants.ASSISTANT_UPDATE_DTO));

        assertTrue(exception.getMessage().contains("Assistant not found with id"));
    }

    @Test
    void update_ShouldThrowResourceAlreadyExistsException_WhenRegistrationNumberAlreadyInUse() {
        Assistant existingAssistant = AssistantConstants.freshAssistantEntity();

        when(assistantRepository.findById(AssistantConstants.ASSISTANT_ID)).thenReturn(Optional.of(existingAssistant));
        when(assistantRepository.existsByRegistrationNumberAndIdNot(AssistantConstants.REGISTRATION_NUMBER, AssistantConstants.ASSISTANT_ID))
                .thenReturn(true);

        ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class,
                () -> assistantService.update(AssistantConstants.ASSISTANT_ID, AssistantConstants.ASSISTANT_UPDATE_DTO));

        assertEquals("Registration Number already in use", exception.getMessage());
    }
}
