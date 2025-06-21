package com.agendador.api_agendador.repository;

import com.agendador.api_agendador.entity.Assistant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssistantRepository extends JpaRepository<Assistant, Long> {
    Optional<Assistant> findByRegistrationNumberIgnoreCase(String registrationNumber);

    boolean existsByRegistrationNumber(String registrationNumber);
    boolean existsByRegistrationNumberAndIdNot(String registrationNumber, Long id);
}