package com.medidesk.repository;

import com.medidesk.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository
        extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatient_EmailOrderByAppointmentTimeAsc(
            String email);

    List<Appointment> findByPatient_EmailAndAppointmentTimeAfterOrderByAppointmentTimeAsc(
            String email, LocalDateTime now);
}