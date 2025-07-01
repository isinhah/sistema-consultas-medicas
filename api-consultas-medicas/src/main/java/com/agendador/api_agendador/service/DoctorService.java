package com.agendador.api_agendador.service;

import com.agendador.api_agendador.entity.Doctor;
import com.agendador.api_agendador.entity.Specialty;
import com.agendador.api_agendador.entity.User;
import com.agendador.api_agendador.entity.enums.Role;
import com.agendador.api_agendador.repository.DoctorRepository;
import com.agendador.api_agendador.repository.SpecialtyRepository;
import com.agendador.api_agendador.repository.UserRepository;
import com.agendador.api_agendador.web.dto.doctor.DoctorCreateDTO;
import com.agendador.api_agendador.web.dto.doctor.DoctorResponseDTO;
import com.agendador.api_agendador.web.dto.doctor.DoctorUpdateDTO;
import com.agendador.api_agendador.web.exception.BadRequestException;
import com.agendador.api_agendador.web.exception.ResourceAlreadyExistsException;
import com.agendador.api_agendador.web.exception.ResourceNotFoundException;
import com.agendador.api_agendador.web.dto.doctor.DoctorMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final SpecialtyRepository specialtyRepository;

    public DoctorService(DoctorRepository doctorRepository, UserRepository userRepository, SpecialtyRepository specialtyRepository) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.specialtyRepository = specialtyRepository;
    }

    @Transactional(readOnly = true)
    public DoctorResponseDTO findById(Long id) {
        Doctor doctor = findEntityById(id);
        return DoctorMapper.INSTANCE.toDto(doctor);
    }

    @Transactional(readOnly = true)
    public Page<DoctorResponseDTO> findAll(Pageable pageable) {
        return doctorRepository.findAll(pageable)
                .map(DoctorMapper.INSTANCE::toDto);
    }

    @Transactional(readOnly = true)
    public DoctorResponseDTO findByCrm(String crm) {
        if (crm == null || crm.isBlank()) {
            throw new BadRequestException("CRM must be provided");
        }

        Doctor doctor = doctorRepository.findByCrmIgnoreCase(crm)
                .orElseThrow(() -> new ResourceNotFoundException("No doctor found with CRM:" + crm));

        return DoctorMapper.INSTANCE.toDto(doctor);
    }

    @Transactional
    public DoctorResponseDTO create(DoctorCreateDTO dto, Long userId) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + userId));

        if (existingUser.getDoctor() != null || existingUser.getPatient() != null || existingUser.getAssistant() != null) {
            throw new ResourceAlreadyExistsException("User already has a profile assigned");
        }
        if (doctorRepository.existsByCrm(dto.crm())) {
            throw new ResourceAlreadyExistsException("CRM already in use");
        }

        Doctor doctor = DoctorMapper.INSTANCE.toEntity(dto);
        doctor.setUser(existingUser);
        doctor.setSpecialties(getSpecialties(dto.specialtyIds()));

        existingUser.setRole(Role.DOCTOR);

        userRepository.save(existingUser);
        doctorRepository.save(doctor);

        return DoctorMapper.INSTANCE.toDto(doctor);
    }

    @Transactional
    public DoctorResponseDTO update(Long userId, DoctorUpdateDTO dto) {
        Doctor doctor = findEntityById(userId);

        if (doctorRepository.existsByCrmAndIdNot(dto.crm(), userId)) {
            throw new ResourceAlreadyExistsException("CRM already in use");
        }

        DoctorMapper.INSTANCE.updateDto(dto, doctor);

        doctor.setUser(doctor.getUser());

        doctor.getSpecialties().clear();
        doctor.getSpecialties().addAll(getSpecialties(dto.specialtyIds()));

        doctorRepository.save(doctor);
        return DoctorMapper.INSTANCE.toDto(doctor);
    }

    @Transactional(readOnly = true)
    public Doctor findEntityById(Long id) {
        return doctorRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Doctor not found with id: " + id)
        );
    }

    Set<Specialty> getSpecialties(Set<Long> specialtyIds) {
        return specialtyIds.stream()
                .map(id -> specialtyRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Specialty not found with id: " + id)))
                .collect(Collectors.toSet());
    }
}