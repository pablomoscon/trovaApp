package com.trova_app.service.emails;

import com.trova_app.dto.email.EmailDTO;
import org.springframework.web.multipart.MultipartFile;

public interface EmailService {

    void sendMessage(EmailDTO emailDTO);
}
