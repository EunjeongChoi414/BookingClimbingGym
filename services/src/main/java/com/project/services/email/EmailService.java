package com.project.services.email;

import com.project.domain.email.EmailRepository;
import com.project.domain.email.EmailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class EmailService {
    private final EmailRepository emailRepository;
    private final EmailSender emailSender;

    public EmailService(EmailRepository emailRepository, EmailSender emailSender) {
        this.emailRepository = emailRepository;
        this.emailSender = emailSender;
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
            //ticket 을 반환한다.
            emailRepository.delete(email);
            return UUID.randomUUID().toString();
        }
    }

}
