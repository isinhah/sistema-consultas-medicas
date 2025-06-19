package com.agendador.api_agendador.repository;

import com.agendador.api_agendador.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByCrm(String crm);
}
