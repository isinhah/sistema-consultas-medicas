package com.agendador.api_agendador.specification;

import com.agendador.api_agendador.entity.Appointment;
import com.agendador.api_agendador.entity.enums.AppointmentStatus;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class AppointmentSpecification {

    /**
     * Specification para encontrar appointments disponíveis por specialtyId.
     * - Só retorna appointments com status AVAILABLE
     * - Só no futuro
     */
    public static Specification<Appointment> availableAppointmentsBySpecialty(Long specialtyId) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            Join<Object, Object> specialties = root.join("doctorSchedule").join("doctor").join("specialties");

            predicate = cb.and(predicate, cb.equal(specialties.get("id"), specialtyId));

            predicate = cb.and(predicate, cb.equal(root.get("status"), AppointmentStatus.AVAILABLE));

            predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("dateTime"), LocalDateTime.now()));

            return predicate;
        };
    }

    /**
     * Specification para filtro dinâmico de Appointment:
     * - por doctorId (via doctorSchedule)
     * - por assistantId
     * - por patientId
     * - por status
     * - por intervalo de dateTime
     */
    public static Specification<Appointment> byFilters(
            Long doctorId,
            Long assistantId,
            Long patientId,
            AppointmentStatus status,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime
    ) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (doctorId != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("doctorSchedule").get("doctor").get("id"), doctorId));
            }

            if (assistantId != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("assistant").get("id"), assistantId));
            }

            if (patientId != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("patient").get("id"), patientId));
            }

            if (status != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("status"), status));
            }

            if (startDateTime != null) {
                predicate = cb.and(predicate,
                        cb.greaterThanOrEqualTo(root.get("dateTime"), startDateTime));
            }

            if (endDateTime != null) {
                predicate = cb.and(predicate,
                        cb.lessThanOrEqualTo(root.get("dateTime"), endDateTime));
            }

            return predicate;
        };
    }

}