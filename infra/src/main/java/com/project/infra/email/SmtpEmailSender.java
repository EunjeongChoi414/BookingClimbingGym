package com.project.infra.email;

import com.project.domain.email.EmailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SmtpEmailSender implements EmailSender {
    @Override
    public void send(String email, String content) {
    }
}
