package com.agendador.api_agendador.repository;

import com.agendador.api_agendador.entity.User;
import com.agendador.api_agendador.entity.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Page<User> findByNameIgnoreCase(String email, Pageable pageable);
    Page<User> findByPhone(String phone, Pageable pageable);
    Page<User> findByRole(Role role, Pageable pageable);
}