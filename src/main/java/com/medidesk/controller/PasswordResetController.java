package com.medidesk.controller;

import com.medidesk.service.PasswordResetService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    public PasswordResetController(
            PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @GetMapping("/forgot-password.html")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String sendOtp(@RequestParam String email) {
        passwordResetService.requestOtp(email);
        return "redirect:/forgot-password.html?sent";
    }

    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestParam String email,
            @RequestParam String otp,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword) {

        if (!newPassword.equals(confirmPassword)) {
            return "redirect:/forgot-password.html?mismatch";
        }

        boolean reset = passwordResetService.resetPassword(
                email,
                otp,
                newPassword
        );

        if (reset) {
            return "redirect:/login.html?reset";
        }

        return "redirect:/forgot-password.html?invalid";
    }
}