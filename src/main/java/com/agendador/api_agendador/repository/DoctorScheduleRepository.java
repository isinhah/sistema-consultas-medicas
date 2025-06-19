package com.agendador.api_agendador.repository;

import com.agendador.api_agendador.entity.DoctorSchedule;
import com.agendador.api_agendador.entity.enums.DayOfWeek;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {
    Page<DoctorSchedule> findByDoctorId(Long doctorId, Pageable pageable);
    Page<DoctorSchedule> findByDayOfWeek(DayOfWeek dayOfWeek, Pageable pageable);
    Page<DoctorSchedule> findByDoctorIdAndDayOfWeek(Long doctorId, DayOfWeek dayOfWeek, Pageable pageable);

    Page<DoctorSchedule> findByDoctorIdAndStartTimeAfter(Long doctorId, LocalTime time, Pageable pageable);
    Page<DoctorSchedule> findByDoctorIdAndEndTimeBefore(Long doctorId, LocalTime time, Pageable pageable);

    @Query("SELECT ds FROM DoctorSchedule ds WHERE ds.doctor.id = :doctorId AND ds.dayOfWeek = :dayOfWeek AND ds.startTime <= :time AND ds.endTime >= :time")
    List<DoctorSchedule> findAvailableByDoctorAndDayAndTime(@Param("doctorId") Long doctorId,
                                                            @Param("dayOfWeek") DayOfWeek dayOfWeek,
                                                            @Param("time") LocalTime time);
}
