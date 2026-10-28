package com.medidesk.config;

import com.medidesk.model.Doctor;
import com.medidesk.repository.DoctorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DoctorDataInitializer implements CommandLineRunner {

    private final DoctorRepository doctorRepository;

    public DoctorDataInitializer(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public void run(String... args) {
        if (doctorRepository.count() == 0) {
            Doctor doctor1 = new Doctor();
            doctor1.setFullName("Dr. Asha Sharma");
            doctor1.setSpecialty("General Physician");

            Doctor doctor2 = new Doctor();
            doctor2.setFullName("Dr. Rohan Mehta");
            doctor2.setSpecialty("Dermatology");

            Doctor doctor3 = new Doctor();
            doctor3.setFullName("Dr. Neha Kapoor");
            doctor3.setSpecialty("Pediatrics");

            doctorRepository.saveAll(List.of(doctor1, doctor2, doctor3));
        }
    }
}