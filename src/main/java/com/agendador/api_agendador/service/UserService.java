package com.agendador.api_agendador.service;

import com.agendador.api_agendador.entity.User;
import com.agendador.api_agendador.entity.enums.Role;
import com.agendador.api_agendador.repository.UserRepository;
import com.agendador.api_agendador.web.dto.UserCreateDTO;
import com.agendador.api_agendador.web.dto.UserResponseDTO;
import com.agendador.api_agendador.web.dto.UserUpdateDTO;
import com.agendador.api_agendador.web.exception.InvalidUserDataException;
import com.agendador.api_agendador.web.exception.UserAlreadyExistsException;
import com.agendador.api_agendador.web.exception.UserNotFoundException;
import com.agendador.api_agendador.web.mapper.UserMapper;
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
    public UserResponseDTO findUser(Long id) {
        User user = findById(id);
        return UserMapper.INSTANCE.toDto(user);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDTO> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(UserMapper.INSTANCE::toDto);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDTO> findByName(String name, Pageable pageable) {
        return userRepository.findByNameIgnoreCase(name, pageable)
                .map(UserMapper.INSTANCE::toDto);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDTO> findByRole(String roleString, Pageable pageable) {
        Role role = Role.fromString(roleString);
        Page<User> users = userRepository.findByRole(role, pageable);
        return users.map(UserMapper.INSTANCE::toDto);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findByEmailOrPhone(String email, String phone) {
        if ((email == null || email.isEmpty()) && (phone == null || phone.isEmpty())) {
            throw new InvalidUserDataException("You must provide at least an email or a phone number");
        }

        User user = userRepository.findByEmailOrPhone(email, phone)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("No user found with email [%s] or phone [%s]", email, phone)
                ));

        return UserMapper.INSTANCE.toDto(user);
    }

    @Transactional
    public UserResponseDTO create(UserCreateDTO dto) {
        if (userRepository.findByEmailOrPhone(dto.email(), dto.phone()).isPresent()) {
            throw new UserAlreadyExistsException("Email or phone already in use");
        }

        User user = UserMapper.INSTANCE.toEntity(dto);
        user.setRole(Role.USER);

        userRepository.save(user);

        return UserMapper.INSTANCE.toDto(user);
    }

    @Transactional
    public UserResponseDTO update(Long id, UserUpdateDTO dto) {
        User user = findById(id);

        if (userRepository.existsByEmailAndIdNot(dto.email(), id)) {
            throw new UserAlreadyExistsException("Email already in use");
        }
        if (userRepository.existsByPhoneAndIdNot(dto.phone(), id)) {
            throw new UserAlreadyExistsException("Phone already in use");
        }

        UserMapper.INSTANCE.updateDto(dto, user);
        User updatedUser = userRepository.save(user);

        return UserMapper.INSTANCE.toDto(updatedUser);
    }

    @Transactional
    public void delete(Long id) {
        User user = findById(id);
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found with id: " + id)
        );
    }
}