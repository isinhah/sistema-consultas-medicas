package com.agendador.api_agendador.service;

import com.agendador.api_agendador.entity.Assistant;
import com.agendador.api_agendador.entity.User;
import com.agendador.api_agendador.entity.enums.Role;
import com.agendador.api_agendador.repository.AssistantRepository;
import com.agendador.api_agendador.repository.UserRepository;
import com.agendador.api_agendador.web.dto.AssistantCreateDTO;
import com.agendador.api_agendador.web.dto.AssistantResponseDTO;
import com.agendador.api_agendador.web.dto.AssistantUpdateDTO;
import com.agendador.api_agendador.web.exception.BadRequestException;
import com.agendador.api_agendador.web.exception.ResourceAlreadyExistsException;
import com.agendador.api_agendador.web.exception.ResourceNotFoundException;
import com.agendador.api_agendador.web.mapper.AssistantMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AssistantService {

    private final AssistantRepository assistantRepository;
    private final UserRepository userRepository;

    public AssistantService(AssistantRepository assistantRepository, UserRepository userRepository) {
        this.assistantRepository = assistantRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public AssistantResponseDTO findById(Long id) {
        Assistant assistant = findEntityById(id);
        return AssistantMapper.INSTANCE.toDto(assistant);
    }

    @Transactional(readOnly = true)
    public Page<AssistantResponseDTO> findAll(Pageable pageable) {
        return assistantRepository.findAll(pageable)
                .map(AssistantMapper.INSTANCE::toDto);
    }

    @Transactional(readOnly = true)
    public AssistantResponseDTO findByRegistrationNumber(String registrationNumber) {
        if (registrationNumber == null || registrationNumber.isBlank()) {
            throw new BadRequestException("Registration Number must be provided");
        }

        Assistant assistant = assistantRepository.findByRegistrationNumberIgnoreCase(registrationNumber)
                .orElseThrow(() -> new ResourceNotFoundException("No assistant found with Registration Number:" + registrationNumber));

        return AssistantMapper.INSTANCE.toDto(assistant);
    }

    @Transactional
    public AssistantResponseDTO create(AssistantCreateDTO dto) {
        User existingUser = userRepository.findById(dto.userId())
                .orElseThrow(() -> new ResourceNotFoundException("Assistant not found with id: " + dto.userId()));

        if (existingUser.getDoctor() != null || existingUser.getPatient() != null || existingUser.getAssistant() != null) {
            throw new ResourceAlreadyExistsException("User already has a profile assigned");
        }
        if (assistantRepository.existsByRegistrationNumber(dto.registrationNumber())) {
            throw new ResourceAlreadyExistsException("CPF already in use");
        }

        Assistant assistant = AssistantMapper.INSTANCE.toEntity(dto);

        assistant.setUser(existingUser);

        existingUser.setRole(Role.ASSISTANT);

        userRepository.save(existingUser);

        assistantRepository.save(assistant);
        return AssistantMapper.INSTANCE.toDto(assistant);
    }

    @Transactional
    public AssistantResponseDTO update(Long id, AssistantUpdateDTO dto) {
        Assistant assistant = findEntityById(id);

        if (assistantRepository.existsByRegistrationNumberAndIdNot(dto.registrationNumber(), id)) {
            throw new ResourceAlreadyExistsException("Registration Number already in use");
        }

        AssistantMapper.INSTANCE.updateDto(dto, assistant);

        assistant.setUser(assistant.getUser());

        Assistant updatedAssistant = assistantRepository.save(assistant);
        return AssistantMapper.INSTANCE.toDto(updatedAssistant);
    }

    @Transactional(readOnly = true)
    public Assistant findEntityById(Long id) {
        return assistantRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Assistant not found with id: " + id)
        );
    }
}
