package com.medidesk.service;

import com.medidesk.model.AppUser;
import com.medidesk.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class RegistrationService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AppUser registerPatient(
            String fullName,
            String email,
            String rawPassword) {

        String cleanEmail = email.trim().toLowerCase(Locale.ROOT);

        if (appUserRepository.existsByEmail(cleanEmail)) {
            throw new IllegalArgumentException(
                    "An account with this email already exists.");
        }

        AppUser account = new AppUser();
        account.setFullName(fullName.trim());
        account.setEmail(cleanEmail);
        account.setPasswordHash(passwordEncoder.encode(rawPassword));

        return appUserRepository.save(account);
    }
}