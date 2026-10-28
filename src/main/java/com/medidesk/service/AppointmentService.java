package com.medidesk.service;

import com.medidesk.model.AppUser;
import com.medidesk.model.Appointment;
import com.medidesk.model.Doctor;
import com.medidesk.repository.AppUserRepository;
import com.medidesk.repository.AppointmentRepository;
import com.medidesk.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppUserRepository appUserRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            AppUserRepository appUserRepository,
            DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.appUserRepository = appUserRepository;
        this.doctorRepository = doctorRepository;
    }

    public Appointment bookAppointment(
            String patientEmail,
            Long doctorId,
            LocalDateTime appointmentTime,
            String reason) {

        if (appointmentTime == null
                || !appointmentTime.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "Choose an appointment time in the future.");
        }

        AppUser patient = appUserRepository.findByEmail(patientEmail)
                .orElseThrow(() ->
                        new IllegalArgumentException("Patient account not found."));

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Doctor not found."));

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentTime(appointmentTime);
        appointment.setReason(reason.trim());

        return appointmentRepository.save(appointment);
    }
}