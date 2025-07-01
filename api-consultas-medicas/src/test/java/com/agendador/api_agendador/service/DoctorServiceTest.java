package com.agendador.api_agendador.service;

import com.agendador.api_agendador.entity.Doctor;
import com.agendador.api_agendador.entity.Specialty;
import com.agendador.api_agendador.entity.User;
import com.agendador.api_agendador.entity.enums.Role;
import com.agendador.api_agendador.repository.DoctorRepository;
import com.agendador.api_agendador.repository.UserRepository;
import com.agendador.api_agendador.util.DoctorConstants;
import com.agendador.api_agendador.util.SpecialtyConstants;
import com.agendador.api_agendador.web.dto.doctor.DoctorResponseDTO;
import com.agendador.api_agendador.web.dto.doctor.DoctorUpdateDTO;
import com.agendador.api_agendador.web.exception.BadRequestException;
import com.agendador.api_agendador.web.exception.ResourceAlreadyExistsException;
import com.agendador.api_agendador.web.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.agendador.api_agendador.util.DoctorConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceTest {

    @InjectMocks
    private DoctorService doctorService;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void findById_ShouldReturnDoctorResponseDTO_WhenSuccessful() {
        when(doctorRepository.findById(DOCTOR_ID)).thenReturn(Optional.of(freshDoctorEntity()));

        DoctorResponseDTO response = doctorService.findById(DOCTOR_ID);

        assertThat(response).usingRecursiveComparison().isEqualTo(DOCTOR_RESPONSE_DTO);
        verify(doctorRepository).findById(DOCTOR_ID);
    }

    @Test
    void findAll_ShouldReturnPageOfDoctorResponseDTO_WhenSuccessful() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Doctor> page = new PageImpl<>(List.of(freshDoctorEntity()), pageable, 1);

        when(doctorRepository.findAll(pageable)).thenReturn(page);

        Page<DoctorResponseDTO> responsePage = doctorService.findAll(pageable);

        assertThat(responsePage.getContent())
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparator()
                .contains(DOCTOR_RESPONSE_DTO);
        verify(doctorRepository).findAll(pageable);
    }

    @Test
    void findByCrm_ShouldReturnDoctorResponseDTO_WhenSuccessful() {
        when(doctorRepository.findByCrmIgnoreCase(CRM_CREATE)).thenReturn(Optional.of(freshDoctorEntity()));

        DoctorResponseDTO response = doctorService.findByCrm(CRM_CREATE);

        assertThat(response).usingRecursiveComparison().isEqualTo(DOCTOR_RESPONSE_DTO);
        verify(doctorRepository).findByCrmIgnoreCase(CRM_CREATE);
    }

    @Test
    void findByCrm_ShouldThrowBadRequest_WhenCrmIsNull() {
        assertThrows(BadRequestException.class, () -> doctorService.findByCrm(null));
    }

    @Test
    void findByCrm_ShouldThrowBadRequestException_WhenCrmIsBlank() {
        assertThrows(BadRequestException.class, () -> doctorService.findByCrm("   "));
    }

    @Test
    void findByCrm_ShouldThrowNotFound_WhenDoctorDoesNotExist() {
        String crm = "MG000000";
        when(doctorRepository.findByCrmIgnoreCase(crm)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> doctorService.findByCrm(crm));
    }

    @Test
    void create_ShouldReturnDoctorResponse_WhenSuccessful() {
        User user = freshDoctorUser();
        user.setRole(Role.USER);

        when(userRepository.findById(DOCTOR_ID)).thenReturn(Optional.of(user));
        when(doctorRepository.existsByCrm(CRM_CREATE)).thenReturn(false);
        when(doctorRepository.save(any(Doctor.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Set<Specialty> specialtiesMock = Set.of(
                SpecialtyConstants.createSpecialty(1L, "Cardiology")
        );

        var spyService = Mockito.spy(doctorService);

        Mockito.doReturn(specialtiesMock).when(spyService).getSpecialties(Mockito.anySet());

        DoctorResponseDTO response = spyService.create(DOCTOR_CREATE_DTO, DOCTOR_ID);

        assertEquals(DOCTOR_RESPONSE_DTO.id(), response.id());
        assertEquals(DOCTOR_RESPONSE_DTO.name(), response.name());
    }

    @Test
    void create_ShouldThrowResourceNotFoundException_WhenUserNotFound() {
        when(userRepository.findById(DOCTOR_ID)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            doctorService.create(DOCTOR_CREATE_DTO, DOCTOR_ID);
        });

        assertEquals("Doctor not found with id: " + DOCTOR_ID, exception.getMessage());
    }

    @Test
    void create_ShouldThrowResourceAlreadyExistsException_WhenUserAlreadyHasProfile() {
        User user = freshDoctorUser();
        user.setDoctor(new Doctor());

        when(userRepository.findById(DOCTOR_ID)).thenReturn(Optional.of(user));

        ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class, () -> {
            doctorService.create(DOCTOR_CREATE_DTO, DOCTOR_ID);
        });

        assertEquals("User already has a profile assigned", exception.getMessage());
    }

    @Test
    void create_ShouldThrowResourceAlreadyExistsException_WhenCrmAlreadyExists() {
        User user = freshDoctorUser();
        user.setRole(Role.USER);

        when(userRepository.findById(DOCTOR_ID)).thenReturn(Optional.of(user));
        when(doctorRepository.existsByCrm(CRM_CREATE)).thenReturn(true);

        ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class, () -> {
            doctorService.create(DOCTOR_CREATE_DTO, DOCTOR_ID);
        });

        assertEquals("CRM already in use", exception.getMessage());
    }

    @Test
    void update_ShouldReturnUpdatedDoctorResponse_WhenSuccessful() {
        Doctor existingDoctor = freshDoctorEntity();
        existingDoctor.setSpecialties(new HashSet<>(specialtiesFromIds(Set.of(1L))));

        when(doctorRepository.findById(DOCTOR_ID)).thenReturn(Optional.of(existingDoctor));
        when(doctorRepository.existsByCrmAndIdNot(DOCTOR_UPDATE_DTO.crm(), DOCTOR_ID)).thenReturn(false);
        when(doctorRepository.save(any(Doctor.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Set<Specialty> specialtiesMock = Set.of(
                SpecialtyConstants.createSpecialty(1L, "Cardiology"),
                SpecialtyConstants.createSpecialty(2L, "Dermatology")
        );

        var spyService = Mockito.spy(doctorService);

        Mockito.doReturn(specialtiesMock).when(spyService).getSpecialties(Mockito.anySet());

        DoctorResponseDTO response = spyService.update(DOCTOR_ID, DOCTOR_UPDATE_DTO);

        assertNotNull(response);
        assertEquals(DOCTOR_UPDATE_DTO.crm(), response.crm());
        assertEquals(DOCTOR_UPDATE_DTO.specialtyIds(), response.specialtyIds());
        verify(doctorRepository).save(existingDoctor);
    }

    @Test
    void update_ShouldThrowResourceAlreadyExistsException_WhenCrmAlreadyInUse() {
        Long userId = DoctorConstants.DOCTOR_ID;
        DoctorUpdateDTO updateDTO = DoctorConstants.DOCTOR_UPDATE_DTO;

        when(doctorRepository.findById(userId)).thenReturn(java.util.Optional.of(DoctorConstants.freshDoctorEntity()));
        when(doctorRepository.existsByCrmAndIdNot(updateDTO.crm(), userId)).thenReturn(true);

        ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class, () -> {
            doctorService.update(userId, updateDTO);
        });

        assertEquals("CRM already in use", exception.getMessage());
    }

    @Test
    void update_ShouldThrowResourceNotFoundException_WhenDoctorNotFound() {
        Long userId = DoctorConstants.DOCTOR_ID;

        when(doctorRepository.findById(userId)).thenReturn(java.util.Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            doctorService.update(userId, DOCTOR_UPDATE_DTO);
        });

        assertTrue(exception.getMessage().contains("Doctor not found"));
    }
}
