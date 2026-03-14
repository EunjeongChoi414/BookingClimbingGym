package com.project.services.email;

import com.project.common.SignUpTicket;
import com.project.domain.email.EmailRepository;
import com.project.domain.email.EmailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class EmailService {
    private final EmailRepository emailRepository;
    private final EmailSender emailSender;
    private final String ticketSecret;

    public EmailService(EmailRepository emailRepository, EmailSender emailSender,
                        @Value("${ticket.secret}") String ticketSecret) {
        this.emailRepository = emailRepository;
        this.emailSender = emailSender;
        this.ticketSecret = ticketSecret;
    }

    public void sendVerificationEmail(String email) {
        String code =  UUID.randomUUID().toString().substring(0, 6);
        emailSender.sendVerificationEmail(email, code);
        emailRepository.save(email, code);
    }

    public String verifyEmail(String email, String code) {
        Optional<String> sentCode = emailRepository.findByEmail(email);
        if(sentCode.isPresent() && !sentCode.get().equals(code)) {
           throw new RuntimeException("잘못된 이메일 코드입니다.");
        } else {
            String ticket = SignUpTicket.issue(email, ticketSecret).getToken();
            emailRepository.delete(email);
            return ticket;
        }
    }

    public void checkIfEmailVerified(String ticket, String email) {
        SignUpTicket parsedTicket = SignUpTicket.parse(ticket, ticketSecret);
        if(!parsedTicket.getEmail().equals(email)) {
            throw new RuntimeException("이메일 검증이 필요합니다.");
        }
    }

}
