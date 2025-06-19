package com.agendador.api_agendador.repository;

import com.agendador.api_agendador.entity.Specialty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialtyRepository extends JpaRepository<Specialty, Long> {
    Page<Specialty> findByNameIgnoreCase(String name, Pageable pageable);
    Page<Specialty> findByDoctorsId(Long doctorId, Pageable pageable);
    Page<Specialty> findByDoctorsCrm(String crm, Pageable pageable);
}