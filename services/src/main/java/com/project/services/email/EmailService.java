package com.project.services.email;

import com.project.common.SignUpTicket;
import com.project.domain.email.EmailRepository;
import com.project.domain.email.EmailSender;
import com.project.domain.email.EmailVerification;
import com.project.domain.exception.EmailCodeMismatchException;
import com.project.domain.exception.NeedEmailVerificationException;
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
        String code = EmailVerification.getVerificationCode();
        emailSender.send(email, code);
        emailRepository.add(email, code);
    }

    public String verifyEmail(String email, String code) {
        String sentCode = emailRepository.findCodeByEmail(email);
        if(!sentCode.equals(code)) {
           throw new EmailCodeMismatchException();
        } else {
            String ticket = SignUpTicket.issue(email, secret).getToken();
            emailRepository.delete(email);
            return ticket;
        }
    }

    public void checkIfEmailVerified(String ticket, String email) {
        SignUpTicket parsedTicket = SignUpTicket.parse(ticket, secret);
        if(!parsedTicket.getEmail().equals(email)) {
            throw new NeedEmailVerificationException();
        }
    }
}
