package com.project.email.port;

public interface EmailSender {
    void send(String email, String content);
}
