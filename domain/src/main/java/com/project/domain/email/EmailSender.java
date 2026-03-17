package com.project.domain.email;

public interface EmailSender {
    void send(String email, String content);
}
