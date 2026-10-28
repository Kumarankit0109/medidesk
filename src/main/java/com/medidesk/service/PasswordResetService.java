package com.medidesk.service;

import com.medidesk.model.AppUser;
import com.medidesk.model.PasswordResetOtp;
import com.medidesk.repository.AppUserRepository;
import com.medidesk.repository.PasswordResetOtpRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Locale;

@Service
public class PasswordResetService {

    private static final int MAX_OTP_ATTEMPTS = 5;

    private final AppUserRepository appUserRepository;
    private final PasswordResetOtpRepository otpRepository;
    private final PasswordResetEmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final SecureRandom secureRandom = new SecureRandom();

    public PasswordResetService(
            AppUserRepository appUserRepository,
            PasswordResetOtpRepository otpRepository,
            PasswordResetEmailService emailService,
            PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.otpRepository = otpRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void requestOtp(String emailAddress) {
        if (emailAddress == null || emailAddress.isBlank()) {
            return;
        }

        String email = emailAddress.trim();
        AppUser user = appUserRepository.findByEmailIgnoreCase(email)
                .orElse(null);

        // Return the same result for unknown addresses to avoid revealing accounts.
        if (user == null) {
            return;
        }

        otpRepository.deleteByUser_EmailIgnoreCase(email);

        String otp = String.format(
                Locale.ROOT,
                "%06d",
                secureRandom.nextInt(1_000_000)
        );

        PasswordResetOtp resetOtp = new PasswordResetOtp(
                user,
                passwordEncoder.encode(otp),
                LocalDateTime.now().plusMinutes(10)
        );

        otpRepository.save(resetOtp);
        emailService.sendOtp(user.getEmail(), otp);
    }

    @Transactional
    public boolean resetPassword(
            String emailAddress,
            String otp,
            String newPassword) {
        if (emailAddress == null || emailAddress.isBlank()
                || otp == null || otp.isBlank()
                || newPassword == null || newPassword.length() < 8) {
            return false;
        }

        String email = emailAddress.trim();

        AppUser user = appUserRepository.findByEmailIgnoreCase(email)
                .orElse(null);

        if (user == null) {
            return false;
        }

        PasswordResetOtp resetOtp = otpRepository
                .findByUser_EmailIgnoreCase(email)
                .orElse(null);

        if (resetOtp == null) {
            return false;
        }

        if (!LocalDateTime.now().isBefore(resetOtp.getExpiresAt())) {
            otpRepository.delete(resetOtp);
            return false;
        }

        if (resetOtp.getAttempts() >= MAX_OTP_ATTEMPTS) {
            otpRepository.delete(resetOtp);
            return false;
        }

        if (!passwordEncoder.matches(otp.trim(), resetOtp.getOtpHash())) {
            resetOtp.setAttempts(resetOtp.getAttempts() + 1);
            otpRepository.save(resetOtp);
            return false;
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        appUserRepository.save(user);
        otpRepository.delete(resetOtp);

        return true;
    }
}