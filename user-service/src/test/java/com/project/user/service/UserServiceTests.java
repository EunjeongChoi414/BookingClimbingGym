package com.project.user.service;

import com.project.common.jwt.AuthToken;
import com.project.user.dto.RegisteredUserInfo;
import com.project.user.entity.User;
import com.project.user.exception.NeedToLoginAgainException;
import com.project.user.exception.NeedToSignupException;
import com.project.user.exception.PasswordMismatchException;
import com.project.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {
    private static final String SECRET = "test-secret-key-that-is-long-enough-for-hmac";
    private final Clock fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

    @Mock
    private UserRepository userRepository;

    private UserService sut;

    @BeforeEach
    void setUp() {
        sut = new UserService(SECRET, userRepository, fixedClock);
    }

    @Test
    void registerUser() {
        String email = "user@example.com";
        String password = "password";

        RegisteredUserInfo userInfo = sut.registerUser(email, password, password);

        assertNotNull(userInfo);
        assertNotNull(userInfo.jwt());
        assertNotNull(userInfo.userId());
    }

    @Test
    void registerUser_failsWhenPasswordMismatch() {
        String email = "user@example.com";
        String password = "password";
        String passwordConfirm = "not-matched-password";

        assertThrows(PasswordMismatchException.class, () -> {
            sut.registerUser(email, password, passwordConfirm);
        });
    }

    @Test
    void loginUser() {
        User user = new User("user@example.com", "password", fixedClock);
        userRepository.create(user);
        when(userRepository.getById(user.getId())).thenReturn(user);
        String userToken = AuthToken.issue(user.getId(), SECRET, fixedClock).getToken();

        String loggedInUserId = sut.loginUser(userToken);

        assertEquals(user.getId(), loggedInUserId);
        assertEquals(user.getLastLoginAt(), LocalDateTime.now(fixedClock));
    }

    @Test
    void loginUser_failsWhenTokenExpired() {
        Clock pastClock = Clock.fixed(Instant.now(fixedClock).minus(31, ChronoUnit.DAYS), ZoneId.systemDefault());
        User user = new User("user@example.com", "password", pastClock);
        String userToken = AuthToken.issue(user.getId(), SECRET, pastClock).getToken();

        assertThrows(NeedToLoginAgainException.class, () -> sut.loginUser(userToken));
    }

    @Test
    void loginUser_failsWhenTokenNotExist() {
        assertThrows(NeedToSignupException.class, () -> sut.loginUser(null));
    }
}
