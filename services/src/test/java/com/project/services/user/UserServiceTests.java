package com.project.services.user;

import com.project.common.AuthToken;
import com.project.domain.exception.NeedToLoginAgainException;
import com.project.domain.exception.NeedToSignupException;
import com.project.domain.user.User;
import com.project.domain.user.UserRepository;
import com.project.services.user.model.RegisteredUserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTests {
    private static final String SECRET = "test-secret-key-that-is-long-enough-for-hmac";
    private final UserRepository userRepository = new com.project.infra.user.UserRepository();
    private final Clock fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());

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
    void loginUser() {
        User user = new User("user@example.com", "password", fixedClock);
        userRepository.create(user);
        String userToken = AuthToken.issue(user.getId(), SECRET, fixedClock).getToken();

        String loggedInUserId = sut.loginUser(userToken);

        assertEquals(user.getId(), loggedInUserId);
        assertEquals(user.getLastLoginAt(), LocalDateTime.now(fixedClock));
    }

    @Test
    void loginUser_failsWhenTokenExpired() {
        Clock pastClock = Clock.fixed(Instant.now(fixedClock).minus(31, ChronoUnit.DAYS), ZoneId.systemDefault());
        User user = new User("user@example.com", "password", pastClock);
        userRepository.create(user);
        String userToken = AuthToken.issue(user.getId(), SECRET, pastClock).getToken();

        assertThrows(NeedToLoginAgainException.class, () -> sut.loginUser(userToken));
    }

    @Test
    void loginUser_failsWhenTokenNotExist() {
        assertThrows(NeedToSignupException.class, () -> sut.loginUser(null));
    }
}