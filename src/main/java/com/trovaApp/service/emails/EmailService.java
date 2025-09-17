package com.trovaApp.service.emails;

import com.trovaApp.dto.email.EmailDTO;
import org.springframework.web.multipart.MultipartFile;

public interface EmailService {

    void sendMessage(EmailDTO emailDTO);
}
