package com.medidesk.controller;

import com.medidesk.repository.SupportTicketRepository;
import com.medidesk.service.SupportTicketService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class SupportTicketController {

    private final SupportTicketService supportTicketService;
    private final SupportTicketRepository supportTicketRepository;

    public SupportTicketController(
            SupportTicketService supportTicketService,
            SupportTicketRepository supportTicketRepository) {
        this.supportTicketService = supportTicketService;
        this.supportTicketRepository = supportTicketRepository;
    }

    @GetMapping("/support.html")
    public String supportPage(
            @AuthenticationPrincipal UserDetails user,
            Model model) {

        model.addAttribute(
                "tickets",
                supportTicketRepository
                        .findByPatient_EmailOrderByCreatedAtDesc(
                                user.getUsername()));

        return "support";
    }

    @PostMapping("/support")
    public String submitTicket(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam("subject") String subject,
            @RequestParam("message") String message,
            @RequestParam(value = "attachment", required = false)
            MultipartFile attachment,
            RedirectAttributes redirectAttributes) {

        try {
            supportTicketService.createTicket(
                    user.getUsername(), subject, message, attachment);
            return "redirect:/support.html?submitted";
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
        } catch (IOException exception) {
            redirectAttributes.addFlashAttribute(
                    "error", "The attachment could not be saved.");
        }

        return "redirect:/support.html";
    }
}