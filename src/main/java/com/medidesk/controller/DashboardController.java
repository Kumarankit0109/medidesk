package com.medidesk.controller;

import com.medidesk.repository.AppointmentRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Controller
public class DashboardController {

    private final AppointmentRepository appointmentRepository;

    public DashboardController(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @GetMapping("/dashboard.html")
    public String dashboard(Authentication authentication, Model model) {
        String email = authentication.getName();

        model.addAttribute("email", email);
        model.addAttribute(
                "upcomingAppointments",
                appointmentRepository
                        .findByPatient_EmailAndAppointmentTimeAfterOrderByAppointmentTimeAsc(
                                email, LocalDateTime.now())
        );

        return "dashboard";
    }
}