package com.project.email.service;

import com.project.common.exception.NeedEmailVerificationException;
import com.project.common.jwt.SignUpTicket;
import com.project.email.entity.Email;
import com.project.email.exception.EmailCodeMismatchException;
import com.project.email.port.EmailSender;
import com.project.email.repository.EmailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailServiceTests {
    private static final String SECRET = "test-secret-key-that-is-long-enough-for-hmac";

    @Mock
    private EmailRepository emailRepository;

    @Mock
    private EmailSender emailSender;

    private EmailService sut;

    @BeforeEach
    void setUp() {
        sut = new EmailService(emailRepository, emailSender, SECRET);
    }

    @Test
    void verifyEmail() {
        String emailAddress = "user@example.com";
        Email email = new Email(emailAddress);
        when(emailRepository.findEmailById(email.getId())).thenReturn(email);
        String code = email.getCode();

        String ticket = sut.verifyEmail(emailAddress, code);

        assertNotNull(ticket);
    }

    @Test
    void verifyEmail_throwsWhenCodeMismatch() {
        String emailAddress = "user@example.com";
        Email email = new Email(emailAddress);
        String code = "wrong-code";
        when(emailRepository.findEmailById(email.getId())).thenReturn(email);

        assertThrows(EmailCodeMismatchException.class,
                () -> sut.verifyEmail(emailAddress, code));
    }

    @Test
    void checkIfEmailVerified() {
        String email = "user@example.com";
        String ticket = SignUpTicket.issue(email, SECRET).getToken();

        assertDoesNotThrow(() -> sut.checkIfEmailVerified(ticket, email));
    }

    @Test
    void checkIfEmailVerified_throwsWhenEmailNotVerified() {
        String ticket = SignUpTicket.issue("other@example.com", SECRET).getToken();
        String email = "user@example.com";

        assertThrows(NeedEmailVerificationException.class,
                () -> sut.checkIfEmailVerified(ticket, email));
    }
}
