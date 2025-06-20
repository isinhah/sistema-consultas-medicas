package com.agendador.api_agendador.service;

import com.agendador.api_agendador.entity.Specialty;
import com.agendador.api_agendador.repository.SpecialtyRepository;
import com.agendador.api_agendador.web.dto.SpecialtyCreateDTO;
import com.agendador.api_agendador.web.dto.SpecialtyResponseDTO;
import com.agendador.api_agendador.web.exception.BadRequestException;
import com.agendador.api_agendador.web.exception.ResourceAlreadyExistsException;
import com.agendador.api_agendador.web.exception.ResourceNotFoundException;
import com.agendador.api_agendador.web.mapper.SpecialtyMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SpecialtyService {

    private final SpecialtyRepository specialtyRepository;

    public SpecialtyService(SpecialtyRepository specialtyRepository) {
        this.specialtyRepository = specialtyRepository;
    }

    @Transactional(readOnly = true)
    public SpecialtyResponseDTO findById(Long id) {
        Specialty specialty = findEntityById(id);
        return SpecialtyMapper.INSTANCE.toDto(specialty);
    }

    @Transactional(readOnly = true)
    public Page<SpecialtyResponseDTO> findAll(Pageable pageable) {
        return specialtyRepository.findAll(pageable)
                .map(SpecialtyMapper.INSTANCE::toDto);
    }

    @Transactional(readOnly = true)
    public SpecialtyResponseDTO findByName(String name) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("Name must be provided");
        }

        Specialty specialty = specialtyRepository.findByNameIgnoreCase(name).orElseThrow(
                () -> new ResourceNotFoundException("Specialty not found with name: " + name));

        return SpecialtyMapper.INSTANCE.toDto(specialty);
    }

    @Transactional(readOnly = true)
    public Page<SpecialtyResponseDTO> findByDoctorId(Long id, Pageable pageable) {
        if (id == null) {
            throw new BadRequestException("Doctor ID must be provided");
        }

        return specialtyRepository.findByDoctorsId(id, pageable)
                .map(SpecialtyMapper.INSTANCE::toDto);
    }

    @Transactional(readOnly = true)
    public Page<SpecialtyResponseDTO> findByDoctorCrm(String crm, Pageable pageable) {
        if (crm == null || crm.isBlank()) {
            throw new BadRequestException("Doctor CRM must be provided");
        }

        return specialtyRepository.findByDoctorsCrm(crm, pageable)
                .map(SpecialtyMapper.INSTANCE::toDto);
    }

    @Transactional
    public SpecialtyResponseDTO create(SpecialtyCreateDTO dto) {
        if (specialtyRepository.existsByName(dto.name())) {
            throw new ResourceAlreadyExistsException("Specialty with this name already exists");
        }

        Specialty specialty = SpecialtyMapper.INSTANCE.toEntity(dto);

        specialtyRepository.save(specialty);

        return SpecialtyMapper.INSTANCE.toDto(specialty);
    }

    @Transactional
    public SpecialtyResponseDTO update(Long id, SpecialtyCreateDTO dto) {
        Specialty specialty = findEntityById(id);

        if (specialtyRepository.existsByNameAndIdNot(dto.name(), id)) {
            throw new ResourceAlreadyExistsException("Specialty with this name already exists");
        }

        SpecialtyMapper.INSTANCE.updateDto(dto, specialty);
        Specialty updatedSpecialty =  specialtyRepository.save(specialty);

        return SpecialtyMapper.INSTANCE.toDto(updatedSpecialty);
    }

    @Transactional
    public void delete(Long id) {
        Specialty specialty = findEntityById(id);
        specialtyRepository.delete(specialty);
    }

    @Transactional(readOnly = true)
    public Specialty findEntityById(Long id) {
        return specialtyRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Specialty not found with id: " + id)
        );
    }
}
