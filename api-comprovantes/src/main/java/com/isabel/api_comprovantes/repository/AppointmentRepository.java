package com.isabel.api_comprovantes.repository;

import com.isabel.api_comprovantes.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
}
