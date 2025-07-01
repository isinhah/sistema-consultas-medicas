package com.agendador.api_agendador.service;

import com.agendador.api_agendador.entity.Specialty;
import com.agendador.api_agendador.repository.SpecialtyRepository;
import com.agendador.api_agendador.util.SpecialtyConstants;
import com.agendador.api_agendador.web.dto.specialty.SpecialtyCreateDTO;
import com.agendador.api_agendador.web.dto.specialty.SpecialtyResponseDTO;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpecialtyServiceTest {

    @InjectMocks
    private SpecialtyService specialtyService;

    @Mock
    private SpecialtyRepository specialtyRepository;

    @Test
    void findById_ShouldReturnSpecialtyResponseDTO_WhenFound() {
        when(specialtyRepository.findById(1L)).thenReturn(Optional.of(SpecialtyConstants.SPECIALTY_CARDIOLOGY));

        SpecialtyResponseDTO dto = specialtyService.findById(1L);

        assertEquals(SpecialtyConstants.SPECIALTY_CARDIOLOGY.getId(), dto.id());
        assertEquals(SpecialtyConstants.SPECIALTY_CARDIOLOGY.getName(), dto.name());
        verify(specialtyRepository).findById(1L);
    }

    @Test
    void findById_ShouldThrowResourceNotFoundException_WhenNotFound() {
        when(specialtyRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            specialtyService.findById(1L);
        });

        assertTrue(ex.getMessage().contains("Specialty not found with id"));
        verify(specialtyRepository).findById(1L);
    }

    @Test
    void findAll_ShouldReturnPageOfSpecialtyResponseDTO() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Specialty> page = new PageImpl<>(List.of(SpecialtyConstants.SPECIALTY_CARDIOLOGY), pageable, 1);

        when(specialtyRepository.findAll(pageable)).thenReturn(page);

        Page<SpecialtyResponseDTO> responsePage = specialtyService.findAll(pageable);

        assertEquals(1, responsePage.getTotalElements());
        assertEquals(SpecialtyConstants.SPECIALTY_CARDIOLOGY.getId(), responsePage.getContent().get(0).id());
        verify(specialtyRepository).findAll(pageable);
    }

    @Test
    void findByName_ShouldReturnSpecialtyResponseDTO_WhenFound() {
        when(specialtyRepository.findByNameIgnoreCase("Cardiology")).thenReturn(Optional.of(SpecialtyConstants.SPECIALTY_CARDIOLOGY));

        SpecialtyResponseDTO dto = specialtyService.findByName("Cardiology");

        assertEquals("Cardiology", dto.name());
        verify(specialtyRepository).findByNameIgnoreCase("Cardiology");
    }

    @Test
    void findByName_ShouldThrowBadRequestException_WhenNameNullOrBlank() {
        assertThrows(BadRequestException.class, () -> specialtyService.findByName(null));
        assertThrows(BadRequestException.class, () -> specialtyService.findByName(""));
        assertThrows(BadRequestException.class, () -> specialtyService.findByName("  "));
    }

    @Test
    void findByName_ShouldThrowResourceNotFoundException_WhenNotFound() {
        when(specialtyRepository.findByNameIgnoreCase("Unknown")).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> specialtyService.findByName("Unknown"));

        assertTrue(ex.getMessage().contains("Specialty not found with name"));
        verify(specialtyRepository).findByNameIgnoreCase("Unknown");
    }

    @Test
    void findByDoctorId_ShouldReturnPageOfSpecialtyResponseDTO() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Specialty> page = new PageImpl<>(List.of(SpecialtyConstants.SPECIALTY_CARDIOLOGY), pageable, 1);

        when(specialtyRepository.findByDoctorsId(1L, pageable)).thenReturn(page);

        Page<SpecialtyResponseDTO> responsePage = specialtyService.findByDoctorId(1L, pageable);

        assertEquals(1, responsePage.getTotalElements());
        verify(specialtyRepository).findByDoctorsId(1L, pageable);
    }

    @Test
    void findByDoctorId_ShouldThrowBadRequestException_WhenIdNull() {
        assertThrows(BadRequestException.class, () -> specialtyService.findByDoctorId(null, PageRequest.of(0, 10)));
        verifyNoInteractions(specialtyRepository);
    }

    @Test
    void findByDoctorCrm_ShouldReturnPageOfSpecialtyResponseDTO() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Specialty> page = new PageImpl<>(List.of(SpecialtyConstants.SPECIALTY_CARDIOLOGY), pageable, 1);

        when(specialtyRepository.findByDoctorsCrm("CRM123", pageable)).thenReturn(page);

        Page<SpecialtyResponseDTO> responsePage = specialtyService.findByDoctorCrm("CRM123", pageable);

        assertEquals(1, responsePage.getTotalElements());
        verify(specialtyRepository).findByDoctorsCrm("CRM123", pageable);
    }

    @Test
    void findByDoctorCrm_ShouldThrowBadRequestException_WhenCrmNullOrBlank() {
        assertThrows(BadRequestException.class, () -> specialtyService.findByDoctorCrm(null, PageRequest.of(0, 10)));
        assertThrows(BadRequestException.class, () -> specialtyService.findByDoctorCrm("", PageRequest.of(0, 10)));
        assertThrows(BadRequestException.class, () -> specialtyService.findByDoctorCrm(" ", PageRequest.of(0, 10)));
        verifyNoInteractions(specialtyRepository);
    }

    @Test
    void create_ShouldReturnSpecialtyResponseDTO_WhenSuccessful() {
        SpecialtyCreateDTO dto = SpecialtyConstants.SPECIALTY_CREATE_DTO;
        Specialty specialtyEntity = SpecialtyConstants.SPECIALTY_CARDIOLOGY;

        when(specialtyRepository.existsByName(dto.name())).thenReturn(false);
        when(specialtyRepository.save(any(Specialty.class))).thenReturn(specialtyEntity);

        SpecialtyResponseDTO responseDTO = specialtyService.create(dto);

        assertEquals(dto.name(), responseDTO.name());
        verify(specialtyRepository).existsByName(dto.name());
        verify(specialtyRepository).save(any(Specialty.class));
    }

    @Test
    void create_ShouldThrowResourceAlreadyExistsException_WhenNameExists() {
        SpecialtyCreateDTO dto = SpecialtyConstants.SPECIALTY_CREATE_DTO;

        when(specialtyRepository.existsByName(dto.name())).thenReturn(true);

        ResourceAlreadyExistsException ex = assertThrows(ResourceAlreadyExistsException.class, () -> specialtyService.create(dto));

        assertTrue(ex.getMessage().contains("Specialty with this name already exists"));
        verify(specialtyRepository).existsByName(dto.name());
        verify(specialtyRepository, never()).save(any());
    }

    @Test
    void delete_ShouldDeleteSpecialty_WhenFound() {
        Specialty specialty = SpecialtyConstants.SPECIALTY_CARDIOLOGY;
        when(specialtyRepository.findById(1L)).thenReturn(Optional.of(specialty));

        specialtyService.delete(1L);

        verify(specialtyRepository).findById(1L);
        verify(specialtyRepository).delete(specialty);
    }

    @Test
    void delete_ShouldThrowResourceNotFoundException_WhenNotFound() {
        when(specialtyRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> specialtyService.delete(1L));

        assertTrue(ex.getMessage().contains("Specialty not found with id"));
        verify(specialtyRepository).findById(1L);
        verify(specialtyRepository, never()).delete(any());
    }
}
