package com.project.services.email;

import com.project.domain.email.EmailSender;
import org.springframework.stereotype.Service;

@Service
public class SmtpEmailSender implements EmailSender {
    @Override
    public void sendVerificationEmail(String email, String content) {
        return;
    }
}
