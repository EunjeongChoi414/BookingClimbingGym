package com.project.domain.email;

public interface EmailSender {
    void sendVerificationEmail(String email, String content);
}
