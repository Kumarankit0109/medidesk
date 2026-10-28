package com.medidesk.controller;

import com.medidesk.repository.DoctorRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DoctorController {

    private final DoctorRepository doctorRepository;

    public DoctorController(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @GetMapping("/doctors.html")
    public String doctors(Model model) {
        model.addAttribute("doctors", doctorRepository.findByActiveTrue());
        return "doctors";
    }
}