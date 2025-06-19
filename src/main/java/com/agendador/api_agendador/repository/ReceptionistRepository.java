package com.agendador.api_agendador.repository;

import com.agendador.api_agendador.entity.Receptionist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReceptionistRepository extends JpaRepository<Receptionist, Long> {
    Optional<Receptionist> findByRegistrationNumber(String registrationNumber);
}