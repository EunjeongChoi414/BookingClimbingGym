package com.project.services.email;

import com.project.common.SignUpTicket;
import com.project.domain.email.EmailRepository;
import com.project.domain.email.EmailSender;
import com.project.domain.exception.EmailCodeMismatchException;
import com.project.domain.exception.NeedEmailVerificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailServiceUnitTests {

    @Mock
    private EmailRepository emailRepository;

    @Mock
    private EmailSender emailSender;

    private EmailService sut;

    private static final String SECRET = "test-secret-key-that-is-long-enough-for-hmac";

    @BeforeEach
    void setUp() {
        sut = new EmailService(emailRepository, emailSender, SECRET);
    }

    @Test
    void verifyEmail() {
        String email = "user@example.com";
        String code = "abc123";
        when(emailRepository.findCodeByEmail(email)).thenReturn(code);

        String ticket = sut.verifyEmail(email, code);

        assertNotNull(ticket);
    }

    @Test
    void verifyEmail_throwsWhenCodeMismatch() {
        String email = "user@example.com";
        String code = "wrong-code";
        when(emailRepository.findCodeByEmail(email)).thenReturn("actual-code");

        assertThrows(EmailCodeMismatchException.class,
                () -> sut.verifyEmail(email, code));
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