package com.trovaApp.controller.email;

import com.trovaApp.dto.email.EmailDTO;
import com.trovaApp.service.emails.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "email", description = "Operations related to email sending")
@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Operation(
            summary = "Send contact message",
            description = "Sends a contact message from the website to the configured email address"
    )
    @PostMapping
    public String sendMessage(@RequestBody EmailDTO contact) {
        emailService.sendMessage(contact);
        return "Message sent successfully";
    }
}
