package com.agendador.api_agendador.service;

import com.agendador.api_agendador.entity.Patient;
import com.agendador.api_agendador.entity.User;
import com.agendador.api_agendador.entity.enums.Role;
import com.agendador.api_agendador.repository.PatientRepository;
import com.agendador.api_agendador.repository.UserRepository;
import com.agendador.api_agendador.web.dto.patient.PatientResponseDTO;
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

import static com.agendador.api_agendador.util.PatientConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @InjectMocks
    private PatientService patientService;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void findById_ShouldReturnPatientResponseDTO_WhenSuccessful() {
        when(patientRepository.findById(PATIENT_ID)).thenReturn(Optional.of(freshPatientEntity()));

        PatientResponseDTO response = patientService.findById(PATIENT_ID);

        assertThat(response).usingRecursiveComparison().isEqualTo(PATIENT_RESPONSE_DTO);
        verify(patientRepository).findById(PATIENT_ID);
    }

    @Test
    void findById_ShouldThrowResourceNotFoundException_WhenNotFound() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            patientService.findById(1L);
        });

        assertTrue(ex.getMessage().contains("Patient not found with id"));
        verify(patientRepository).findById(1L);
    }

    @Test
    void findAll_ShouldReturnPageOfPatientResponseDTO_WhenSuccessful() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Patient> page = new PageImpl<>(List.of(freshPatientEntity()), pageable, 1);

        when(patientRepository.findAll(pageable)).thenReturn(page);

        Page<PatientResponseDTO> responsePage = patientService.findAll(pageable);

        assertThat(responsePage.getContent())
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparator()
                .contains(PATIENT_RESPONSE_DTO);
        verify(patientRepository).findAll(pageable);
    }

    @Test
    void findByCpf_ShouldReturnPatientResponseDTO_WhenSuccessful() {
        when(patientRepository.findByCpf(CPF)).thenReturn(Optional.of(freshPatientEntity()));

        PatientResponseDTO response = patientService.findByCpf(CPF);

        assertThat(response).usingRecursiveComparison().isEqualTo(PATIENT_RESPONSE_DTO);
        verify(patientRepository).findByCpf(CPF);
    }

    @Test
    void findByCpf_ShouldThrowBadRequestException_WhenCpfIsNull() {
        assertThrows(BadRequestException.class, () -> patientService.findByCpf(null));
    }

    @Test
    void findByCpf_ShouldThrowBadRequestException_WhenCpfIsBlank() {
        assertThrows(BadRequestException.class, () -> patientService.findByCpf("   "));
    }

    @Test
    void findByCpf_ShouldThrowResourceNotFoundException_WhenPatientNotFound() {
        when(patientRepository.findByCpf(CPF)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> patientService.findByCpf(CPF));
    }

    @Test
    void create_ShouldReturnPatientResponseDTO_WhenSuccessful() {
        User user = freshPatientUser();
        user.setRole(Role.USER);

        when(userRepository.findById(PATIENT_ID)).thenReturn(Optional.of(user));
        when(patientRepository.existsByCpf(PATIENT_CREATE_DTO.cpf())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        PatientResponseDTO response = patientService.create(PATIENT_CREATE_DTO, PATIENT_ID);

        assertThat(response.cpf()).isEqualTo(PATIENT_CREATE_DTO.cpf());
        verify(userRepository).findById(PATIENT_ID);
        verify(patientRepository).existsByCpf(PATIENT_CREATE_DTO.cpf());
        verify(userRepository).save(any(User.class));
        verify(patientRepository).save(any(Patient.class));
    }

    @Test
    void create_ShouldThrowResourceNotFoundException_WhenUserNotFound() {
        when(userRepository.findById(PATIENT_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> patientService.create(PATIENT_CREATE_DTO, PATIENT_ID));

        verify(userRepository).findById(PATIENT_ID);
    }

    @Test
    void create_ShouldThrowResourceAlreadyExistsException_WhenUserHasProfile() {
        User user = freshPatientUser();
        user.setPatient(freshPatientEntity());

        when(userRepository.findById(PATIENT_ID)).thenReturn(Optional.of(user));

        ResourceAlreadyExistsException ex = assertThrows(ResourceAlreadyExistsException.class,
                () -> patientService.create(PATIENT_CREATE_DTO, PATIENT_ID));
        assertThat(ex.getMessage()).contains("User already has a profile assigned");

        verify(userRepository).findById(PATIENT_ID);
    }

    @Test
    void create_ShouldThrowResourceAlreadyExistsException_WhenCpfExists() {
        User user = freshPatientUser();

        when(userRepository.findById(PATIENT_ID)).thenReturn(Optional.of(user));
        when(patientRepository.existsByCpf(PATIENT_CREATE_DTO.cpf())).thenReturn(true);

        ResourceAlreadyExistsException ex = assertThrows(ResourceAlreadyExistsException.class,
                () -> patientService.create(PATIENT_CREATE_DTO, PATIENT_ID));
        assertThat(ex.getMessage()).contains("CPF already in use");

        verify(userRepository).findById(PATIENT_ID);
        verify(patientRepository).existsByCpf(PATIENT_CREATE_DTO.cpf());
    }

    @Test
    void update_ShouldReturnUpdatedPatientResponseDTO_WhenSuccessful() {
        when(patientRepository.findById(PATIENT_ID)).thenReturn(Optional.of(freshPatientEntity()));
        when(patientRepository.existsByCpfAndIdNot(PATIENT_UPDATE_DTO.cpf(), PATIENT_ID)).thenReturn(false);
        when(patientRepository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PatientResponseDTO response = patientService.update(PATIENT_ID, PATIENT_UPDATE_DTO);

        assertThat(response.cpf()).isEqualTo(PATIENT_UPDATE_DTO.cpf());
        verify(patientRepository).findById(PATIENT_ID);
        verify(patientRepository).existsByCpfAndIdNot(PATIENT_UPDATE_DTO.cpf(), PATIENT_ID);
        verify(patientRepository).save(any(Patient.class));
    }

    @Test
    void update_ShouldThrowResourceNotFoundException_WhenPatientNotFound() {
        when(patientRepository.findById(PATIENT_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> patientService.update(PATIENT_ID, PATIENT_UPDATE_DTO));

        verify(patientRepository).findById(PATIENT_ID);
    }

    @Test
    void update_ShouldThrowResourceAlreadyExistsException_WhenCpfDuplicated() {
        when(patientRepository.findById(PATIENT_ID)).thenReturn(Optional.of(freshPatientEntity()));
        when(patientRepository.existsByCpfAndIdNot(PATIENT_UPDATE_DTO.cpf(), PATIENT_ID)).thenReturn(true);

        ResourceAlreadyExistsException ex = assertThrows(ResourceAlreadyExistsException.class,
                () -> patientService.update(PATIENT_ID, PATIENT_UPDATE_DTO));
        assertThat(ex.getMessage()).contains("CPF already in use");

        verify(patientRepository).findById(PATIENT_ID);
        verify(patientRepository).existsByCpfAndIdNot(PATIENT_UPDATE_DTO.cpf(), PATIENT_ID);
    }
}
