package com.trova_app.service.emails;

import com.trova_app.dto.email.EmailDTO;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String appEmail;

    @Override
    public void sendMessage(EmailDTO emailDTO) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setFrom(appEmail);
            helper.setTo(appEmail);

            helper.setSubject("New contact message from your website");

            StringBuilder sb = new StringBuilder();
            sb.append("First Name: ").append(emailDTO.getFirstName()).append("\n");
            sb.append("Last Name: ").append(emailDTO.getLastName()).append("\n");
            sb.append("Email: ").append(emailDTO.getEmail()).append("\n");
            if (emailDTO.getPhone() != null && !emailDTO.getPhone().isEmpty()) {
                sb.append("Phone: ").append(emailDTO.getPhone()).append("\n");
            }
            sb.append("\nMessage:\n").append(emailDTO.getMessage());

            helper.setText(sb.toString(), false);

            // helper.setReplyTo(emailDTO.getEmail());

            mailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
