package com.agendador.api_agendador.service;

import com.agendador.api_agendador.entity.Receptionist;
import com.agendador.api_agendador.entity.User;
import com.agendador.api_agendador.entity.enums.Role;
import com.agendador.api_agendador.repository.ReceptionistRepository;
import com.agendador.api_agendador.repository.UserRepository;
import com.agendador.api_agendador.web.dto.ReceptionistCreateDTO;
import com.agendador.api_agendador.web.dto.ReceptionistResponseDTO;
import com.agendador.api_agendador.web.dto.ReceptionistUpdateDTO;
import com.agendador.api_agendador.web.exception.BadRequestException;
import com.agendador.api_agendador.web.exception.ResourceAlreadyExistsException;
import com.agendador.api_agendador.web.exception.ResourceNotFoundException;
import com.agendador.api_agendador.web.mapper.ReceptionistMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReceptionistService {

    private final ReceptionistRepository  receptionistRepository;
    private final UserRepository userRepository;

    public ReceptionistService(ReceptionistRepository receptionistRepository, UserRepository userRepository) {
        this.receptionistRepository = receptionistRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public ReceptionistResponseDTO findById(Long id) {
        Receptionist receptionist = findEntityById(id);
        return ReceptionistMapper.INSTANCE.toDto(receptionist);
    }

    @Transactional(readOnly = true)
    public Page<ReceptionistResponseDTO> findAll(Pageable pageable) {
        return receptionistRepository.findAll(pageable)
                .map(ReceptionistMapper.INSTANCE::toDto);
    }

    @Transactional(readOnly = true)
    public ReceptionistResponseDTO findByRegistrationNumber(String registrationNumber) {
        if (registrationNumber == null || registrationNumber.isBlank()) {
            throw new BadRequestException("Registration Number must be provided");
        }

        Receptionist receptionist = receptionistRepository.findByRegistrationNumberIgnoreCase(registrationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("No receptionist found with Registration Number:" + registrationNumber));

        return ReceptionistMapper.INSTANCE.toDto(receptionist);
    }

    @Transactional
    public ReceptionistResponseDTO create(ReceptionistCreateDTO dto) {
        User existingUser = userRepository.findById(dto.userId())
                .orElseThrow(() -> new ResourceNotFoundException("Receptionist not found with id: " + dto.userId()));

        if (existingUser.getDoctor() != null || existingUser.getPatient() != null || existingUser.getReceptionist() != null) {
            throw new ResourceAlreadyExistsException("User already has a profile assigned");
        }
        if (receptionistRepository.existsByRegistrationNumber(dto.registrationNumber())) {
            throw new ResourceAlreadyExistsException("CPF already in use");
        }

        Receptionist receptionist = ReceptionistMapper.INSTANCE.toEntity(dto);

        receptionist.setUser(existingUser);

        existingUser.setRole(Role.RECEPTIONIST);

        userRepository.save(existingUser);

        receptionistRepository.save(receptionist);
        return ReceptionistMapper.INSTANCE.toDto(receptionist);
    }

    @Transactional
    public ReceptionistResponseDTO update(Long id, ReceptionistUpdateDTO dto) {
        Receptionist receptionist = findEntityById(id);

        if (receptionistRepository.existsByRegistrationNumberAndIdNot(dto.registrationNumber(), id)) {
            throw new ResourceAlreadyExistsException("Registration Number already in use");
        }

        ReceptionistMapper.INSTANCE.updateDto(dto, receptionist);

        receptionist.setUser(receptionist.getUser());

        Receptionist updatedReceptionist = receptionistRepository.save(receptionist);
        return ReceptionistMapper.INSTANCE.toDto(updatedReceptionist);
    }

    @Transactional(readOnly = true)
    public Receptionist findEntityById(Long id) {
        return receptionistRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Receptionist not found with id: " + id)
        );
    }
}
