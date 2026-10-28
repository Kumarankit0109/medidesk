package com.medidesk.service;

import com.medidesk.model.AppUser;
import com.medidesk.model.SupportTicket;
import com.medidesk.repository.AppUserRepository;
import com.medidesk.repository.SupportTicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class SupportTicketService {

    private final SupportTicketRepository supportTicketRepository;
    private final AppUserRepository appUserRepository;
    private final FileStorageService fileStorageService;

    public SupportTicketService(
            SupportTicketRepository supportTicketRepository,
            AppUserRepository appUserRepository,
            FileStorageService fileStorageService) {
        this.supportTicketRepository = supportTicketRepository;
        this.appUserRepository = appUserRepository;
        this.fileStorageService = fileStorageService;
    }

    public SupportTicket createTicket(
            String patientEmail,
            String subject,
            String message,
            MultipartFile attachment) throws IOException {

        if (subject == null || subject.isBlank()
                || message == null || message.isBlank()) {
            throw new IllegalArgumentException(
                    "Enter a subject and a message.");
        }

        AppUser patient = appUserRepository.findByEmail(patientEmail)
                .orElseThrow(() ->
                        new IllegalArgumentException("Patient account not found."));

        SupportTicket ticket = new SupportTicket();
        ticket.setPatient(patient);
        ticket.setSubject(subject.trim());
        ticket.setMessage(message.trim());

        if (attachment != null && !attachment.isEmpty()) {
            String storedName = fileStorageService.store(attachment);
            String originalName = StringUtils.cleanPath(
                    attachment.getOriginalFilename() == null
                            ? "attachment"
                            : attachment.getOriginalFilename());

            ticket.setAttachmentOriginalName(originalName);
            ticket.setAttachmentStoredName(storedName);
        }

        return supportTicketRepository.save(ticket);
    }
}