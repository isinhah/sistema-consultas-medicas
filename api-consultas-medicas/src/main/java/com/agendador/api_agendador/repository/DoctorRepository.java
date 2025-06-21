package com.agendador.api_agendador.repository;

import com.agendador.api_agendador.entity.Doctor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByCrmIgnoreCase(String crm);

    boolean existsByCrm(String crm);
    boolean existsByCrmAndIdNot(String crm, Long id);
}