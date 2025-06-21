package com.agendador.api_agendador.service;

import com.agendador.api_agendador.entity.User;
import com.agendador.api_agendador.entity.enums.Role;
import com.agendador.api_agendador.repository.UserRepository;
import com.agendador.api_agendador.web.dto.user.UserCreateDTO;
import com.agendador.api_agendador.web.dto.user.UserResponseDTO;
import com.agendador.api_agendador.web.dto.user.UserUpdateDTO;
import com.agendador.api_agendador.web.exception.BadRequestException;
import com.agendador.api_agendador.web.exception.ResourceAlreadyExistsException;
import com.agendador.api_agendador.web.exception.ResourceNotFoundException;
import com.agendador.api_agendador.web.dto.user.UserMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findById(Long id) {
        User user = findEntityById(id);
        return UserMapper.INSTANCE.toDto(user);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDTO> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(UserMapper.INSTANCE::toDto);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDTO> findByName(String name, Pageable pageable) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("Name must be provided");
        }

        return userRepository.findByNameIgnoreCase(name, pageable)
                .map(UserMapper.INSTANCE::toDto);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDTO> findByRole(String roleString, Pageable pageable) {
        if (roleString == null || roleString.isBlank()) {
            throw new BadRequestException("Role must be provided");
        }

        Role role = Role.fromString(roleString);
        Page<User> users = userRepository.findByRole(role, pageable);
        return users.map(UserMapper.INSTANCE::toDto);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findByEmailOrPhone(String email, String phone) {
        if ((email == null || email.isEmpty()) && (phone == null || phone.isEmpty())) {
            throw new BadRequestException("You must provide at least an email or a phone number");
        }

        User user = userRepository.findByEmailOrPhone(email, phone)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("No user found with email [%s] or phone [%s]", email, phone)
                ));

        return UserMapper.INSTANCE.toDto(user);
    }

    @Transactional
    public UserResponseDTO create(UserCreateDTO dto) {
        if (userRepository.existsByEmailOrPhone(dto.email(), dto.phone())) {
            throw new ResourceAlreadyExistsException("Email or phone already in use");
        }

        User user = UserMapper.INSTANCE.toEntity(dto);
        user.setRole(Role.USER);

        userRepository.save(user);

        return UserMapper.INSTANCE.toDto(user);
    }

    @Transactional
    public UserResponseDTO update(Long id, UserUpdateDTO dto) {
        User user = findEntityById(id);

        if (userRepository.existsByEmailAndIdNot(dto.email(), id)) {
            throw new ResourceAlreadyExistsException("Email already in use");
        }
        if (userRepository.existsByPhoneAndIdNot(dto.phone(), id)) {
            throw new ResourceAlreadyExistsException("Phone already in use");
        }

        UserMapper.INSTANCE.updateDto(dto, user);
        User updatedUser = userRepository.save(user);

        return UserMapper.INSTANCE.toDto(updatedUser);
    }

    @Transactional
    public void delete(Long id) {
        User user = findEntityById(id);
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public User findEntityById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User not found with id: " + id)
        );
    }
}