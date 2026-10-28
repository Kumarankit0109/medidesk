package com.medidesk.controller;

import com.medidesk.repository.AppointmentRepository;
import com.medidesk.repository.DoctorRepository;
import com.medidesk.service.AppointmentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentController(
            AppointmentService appointmentService,
            AppointmentRepository appointmentRepository,
            DoctorRepository doctorRepository) {
        this.appointmentService = appointmentService;
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
    }

    @GetMapping("/appointments/new")
    public String bookingForm(
            @RequestParam("doctorId") Long doctorId,
            Model model) {

        var doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Doctor not found."));

        model.addAttribute("doctor", doctor);
        return "appointment-form";
    }

    @PostMapping("/appointments")
    public String bookAppointment(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam("doctorId") Long doctorId,
            @RequestParam("appointmentTime")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime appointmentTime,
            @RequestParam("reason") String reason,
            RedirectAttributes redirectAttributes) {

        try {
            appointmentService.bookAppointment(
                    user.getUsername(), doctorId, appointmentTime, reason);
            return "redirect:/appointments.html?booked";
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addAttribute("doctorId", doctorId);
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
            return "redirect:/appointments/new";
        }
    }

    @GetMapping("/appointments.html")
    public String appointments(
            @AuthenticationPrincipal UserDetails user,
            Model model) {

        model.addAttribute(
                "appointments",
                appointmentRepository
                        .findByPatient_EmailOrderByAppointmentTimeAsc(
                                user.getUsername()));

        return "appointments";
    }
}