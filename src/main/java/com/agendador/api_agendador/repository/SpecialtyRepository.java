package com.agendador.api_agendador.repository;

import com.agendador.api_agendador.entity.Specialty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpecialtyRepository extends JpaRepository<Specialty, Long> {
    Optional<Specialty> findByNameIgnoreCase(String name);

    Page<Specialty> findByDoctorsId(Long doctorId, Pageable pageable);
    Page<Specialty> findByDoctorsCrm(String crm, Pageable pageable);

    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
}