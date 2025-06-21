package com.agendador.api_agendador.specification;

import com.agendador.api_agendador.entity.Appointment;
import com.agendador.api_agendador.entity.DoctorSchedule;
import com.agendador.api_agendador.entity.enums.DayOfWeek;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalTime;

public class DoctorScheduleSpecification {

    /**
     * Specification para filtro dinâmico por doctorId, dayOfWeek, startTime e endTime opcionais.
     **/
    public static Specification<DoctorSchedule> byDoctorIdAndOptionalFilters(Long doctorId, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime)
    {
        return (root, query, cb) -> {
            Predicate predicate = cb.equal(root.get("doctor").get("id"), doctorId);

            if (dayOfWeek != null) {
                predicate = cb.and(predicate, cb.equal(root.get("dayOfWeek"), dayOfWeek));
            }

            if (startTime != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("startTime"), startTime));
            }

            if (endTime != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("endTime"), endTime));
            }

            return predicate;
        };
    }

    /**
     * Specification para encontrar doctor schedules disponíveis por specialty id
     **/
    public static Specification<DoctorSchedule> availableSchedulesBySpecialty(Long specialtyId)
    {
        return (root, query, cb) -> {
            Join<Object, Object> specialtyJoin = root.join("doctor").join("specialties");

            assert query != null;
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<?> appointmentRoot = subquery.from(Appointment.class);
            subquery.select(appointmentRoot.get("doctorSchedule").get("id"));

            Predicate specialtyPredicate = cb.equal(specialtyJoin.get("id"), specialtyId);

            Predicate notInAppointments = cb.not(root.get("id").in(subquery));

            Predicate specialtyAndNotBooked = cb.and(specialtyPredicate, notInAppointments);

            return specialtyAndNotBooked;
        };
    }
}