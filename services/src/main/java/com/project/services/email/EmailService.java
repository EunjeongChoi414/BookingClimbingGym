package com.project.services.email;

import com.project.common.SignUpTicket;
import com.project.domain.email.Email;
import com.project.domain.email.EmailRepository;
import com.project.domain.email.EmailSender;
import com.project.domain.exception.DomainException;
import com.project.domain.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final EmailRepository emailRepository;
    private final EmailSender emailSender;
    private final String secret;

    public EmailService(EmailRepository emailRepository, EmailSender emailSender,
                        @Value("${ticket.secret}") String secret) {
        this.emailRepository = emailRepository;
        this.emailSender = emailSender;
        this.secret = secret;
    }

    public void sendVerificationCode(String email) {
        Email newEmail = new Email(email);
        String code = newEmail.getCode();
        emailSender.send(email, code);
        emailRepository.add(newEmail);
    }

    public String verifyEmail(String email, String code) {
        Email savedEmail = emailRepository.findEmailById(email);
        if (!savedEmail.getCode().equals(code)) {
            throw new DomainException(ErrorCode.INVALID_EMAIL_CODE);
        } else {
            String ticket = SignUpTicket.issue(email, secret).getToken();
            emailRepository.delete(savedEmail);
            return ticket;
        }
    }

    public void checkIfEmailVerified(String ticket, String email) {
        SignUpTicket parsedTicket = SignUpTicket.parse(ticket, secret);
        if (!parsedTicket.getEmail().equals(email)) {
            throw new DomainException(ErrorCode.NEED_EMAIL_VERIFICATION);
        }
    }
}
