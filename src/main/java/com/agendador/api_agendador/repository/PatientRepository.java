package com.agendador.api_agendador.repository;

import com.agendador.api_agendador.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByMedicalRecordNumber(String medicalRecordNumber);
}