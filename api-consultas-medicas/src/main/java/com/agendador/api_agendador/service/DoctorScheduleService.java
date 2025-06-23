package com.agendador.api_agendador.service;

import com.agendador.api_agendador.entity.Doctor;
import com.agendador.api_agendador.entity.DoctorSchedule;
import com.agendador.api_agendador.entity.enums.DayOfWeek;
import com.agendador.api_agendador.repository.DoctorRepository;
import com.agendador.api_agendador.repository.DoctorScheduleRepository;
import com.agendador.api_agendador.specification.DoctorScheduleSpecification;
import com.agendador.api_agendador.web.dto.doctor_schedule.DoctorScheduleCreateDTO;
import com.agendador.api_agendador.web.dto.doctor_schedule.DoctorScheduleResponseDTO;
import com.agendador.api_agendador.web.dto.doctor_schedule.DoctorScheduleUpdateDTO;
import com.agendador.api_agendador.web.exception.BadRequestException;
import com.agendador.api_agendador.web.exception.ResourceAlreadyExistsException;
import com.agendador.api_agendador.web.exception.ResourceNotFoundException;
import com.agendador.api_agendador.web.dto.doctor_schedule.DoctorScheduleMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@Service
public class DoctorScheduleService {

    private final DoctorScheduleRepository doctorScheduleRepository;
    private final DoctorRepository doctorRepository;

    public DoctorScheduleService(DoctorScheduleRepository doctorScheduleRepository, DoctorRepository doctorRepository) {
        this.doctorScheduleRepository = doctorScheduleRepository;
        this.doctorRepository = doctorRepository;
    }

    @Transactional(readOnly = true)
    public DoctorScheduleResponseDTO findById(Long id) {
        DoctorSchedule doctorSchedule = findEntityById(id);
        return DoctorScheduleMapper.INSTANCE.toDto(doctorSchedule);
    }

    @Transactional(readOnly = true)
    public Page<DoctorScheduleResponseDTO> findSchedulesByDoctorId(Long doctorId, DayOfWeek dayOfWeek,
                                                    LocalTime startTime, LocalTime endTime,
                                                    Pageable pageable) {
        Specification<DoctorSchedule> spec = DoctorScheduleSpecification.byDoctorIdAndOptionalFilters(doctorId, dayOfWeek, startTime, endTime);
        Page<DoctorSchedule> page = doctorScheduleRepository.findAll(spec, pageable);
        return page.map(DoctorScheduleMapper.INSTANCE::toDto);
    }

    @Transactional(readOnly = true)
    public Page<DoctorScheduleResponseDTO> findAvailableSchedulesBySpecialty(Long specialtyId, Pageable pageable) {
        Specification<DoctorSchedule> spec = DoctorScheduleSpecification.availableSchedulesBySpecialty(specialtyId);
        Page<DoctorSchedule> page = doctorScheduleRepository.findAll(spec, pageable);
        return page.map(DoctorScheduleMapper.INSTANCE::toDto);
    }

    @Transactional
    public DoctorScheduleResponseDTO create(DoctorScheduleCreateDTO dto) {
        if (!doctorRepository.existsById(dto.doctorId())) {
            throw new ResourceNotFoundException("Doctor not found with id: " + dto.doctorId());
        }

        validateScheduleConflict(dto.doctorId(), dto.dayOfWeek(), dto.startTime(), dto.endTime(), null);

        Doctor doctor = doctorRepository.getReferenceById(dto.doctorId());

        DoctorSchedule doctorSchedule = DoctorScheduleMapper.INSTANCE.toEntity(dto);
        doctorSchedule.setDoctor(doctor);

        doctorScheduleRepository.save(doctorSchedule);
        return DoctorScheduleMapper.INSTANCE.toDto(doctorSchedule);
    }

    @Transactional
    public DoctorScheduleResponseDTO update(Long id, DoctorScheduleUpdateDTO dto) {
        DoctorSchedule doctorSchedule = findEntityById(id);
        Long doctorId = doctorSchedule.getDoctor().getId();

        validateScheduleConflict(doctorId, dto.dayOfWeek(), dto.startTime(), dto.endTime(), id);

        DoctorScheduleMapper.INSTANCE.updateDto(dto, doctorSchedule);

        DoctorSchedule updated = doctorScheduleRepository.save(doctorSchedule);
        return DoctorScheduleMapper.INSTANCE.toDto(updated);
    }

    @Transactional
    public void delete(Long id) {
        DoctorSchedule schedule = findEntityById(id);

        if (!schedule.getAppointments().isEmpty()) {
            throw new BadRequestException(
                    "Cannot delete this doctor schedule because it has associated appointments."
            );
        }

        doctorScheduleRepository.delete(schedule);
    }

    private void validateScheduleConflict(Long doctorId, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, Long excludeId) {
        boolean exists;
        if (excludeId == null) {
            exists = doctorScheduleRepository.existsByDoctorIdAndDayOfWeekAndStartTimeLessThanAndEndTimeGreaterThan(
                    doctorId, dayOfWeek, endTime, startTime);
        } else {
            exists = doctorScheduleRepository.existsByDoctorIdAndDayOfWeekAndStartTimeLessThanAndEndTimeGreaterThanAndIdNot(
                    doctorId, dayOfWeek, endTime, startTime, excludeId);
        }

        if (exists) {
            throw new ResourceAlreadyExistsException("A doctor schedule already exists for this doctor at the specified time.");
        }
    }

    @Transactional(readOnly = true)
    public DoctorSchedule findEntityById(Long id) {
        return doctorScheduleRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Doctor Schedule not found with id: " + id)
        );
    }
}
