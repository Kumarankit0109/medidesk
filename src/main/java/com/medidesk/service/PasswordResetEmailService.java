package com.medidesk.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetEmailService {

    private final JavaMailSender mailSender;

    public PasswordResetEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtp(String recipientEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("MediDesk password reset code");
        message.setText(
                "Your MediDesk password reset code is: " + otp
                        + "\n\nThis code expires in 10 minutes."
                        + "\nIf you did not request a password reset, ignore this email."
        );

        mailSender.send(message);
    }
}