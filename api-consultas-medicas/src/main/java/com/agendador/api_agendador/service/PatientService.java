package com.agendador.api_agendador.service;

import com.agendador.api_agendador.entity.Patient;
import com.agendador.api_agendador.entity.User;
import com.agendador.api_agendador.entity.enums.Role;
import com.agendador.api_agendador.repository.PatientRepository;
import com.agendador.api_agendador.repository.UserRepository;
import com.agendador.api_agendador.web.dto.patient.PatientCreateDTO;
import com.agendador.api_agendador.web.dto.patient.PatientResponseDTO;
import com.agendador.api_agendador.web.dto.patient.PatientUpdateDTO;
import com.agendador.api_agendador.web.exception.BadRequestException;
import com.agendador.api_agendador.web.exception.ResourceAlreadyExistsException;
import com.agendador.api_agendador.web.exception.ResourceNotFoundException;
import com.agendador.api_agendador.web.dto.patient.PatientMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public PatientService(PatientRepository patientRepository, UserRepository userRepository) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public PatientResponseDTO findById(Long id) {
        Patient patient = findEntityById(id);
        return PatientMapper.INSTANCE.toDto(patient);
    }

    @Transactional(readOnly = true)
    public Page<PatientResponseDTO> findAll(Pageable pageable) {
        return patientRepository.findAll(pageable)
                .map(PatientMapper.INSTANCE::toDto);
    }

    @Transactional(readOnly = true)
    public PatientResponseDTO findByCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw new BadRequestException("CPF must be provided");
        }

        Patient patient = patientRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("No patient found with CPF:" + cpf));

        return PatientMapper.INSTANCE.toDto(patient);
    }

    @Transactional
    public PatientResponseDTO create(PatientCreateDTO dto, Long userId) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + userId));

        if (existingUser.getDoctor() != null || existingUser.getPatient() != null || existingUser.getAssistant() != null) {
            throw new ResourceAlreadyExistsException("User already has a profile assigned");
        }
        if (patientRepository.existsByCpf(dto.cpf())) {
            throw new ResourceAlreadyExistsException("CPF already in use");
        }

        Patient patient = PatientMapper.INSTANCE.toEntity(dto);

        patient.setUser(existingUser);

        existingUser.setRole(Role.PATIENT);

        userRepository.save(existingUser);

        patientRepository.save(patient);
        return PatientMapper.INSTANCE.toDto(patient);
    }

    @Transactional
    public PatientResponseDTO update(Long id, PatientUpdateDTO dto) {
        Patient patient = findEntityById(id);

        if (patientRepository.existsByCpfAndIdNot(dto.cpf(), id)) {
            throw new ResourceAlreadyExistsException("CPF already in use");
        }

        PatientMapper.INSTANCE.updateDto(dto, patient);

        patient.setUser(patient.getUser());

        Patient updatedPatient = patientRepository.save(patient);
        return PatientMapper.INSTANCE.toDto(updatedPatient);
    }

    @Transactional(readOnly = true)
    public Patient findEntityById(Long id) {
        return patientRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Patient not found with id: " + id)
        );
    }
}