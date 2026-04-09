package com.project.user.service;

import com.project.common.jwt.AuthToken;
import com.project.user.dto.RegisteredUserInfo;
import com.project.user.entity.User;
import com.project.user.exception.NeedToSignupException;
import com.project.user.exception.PasswordMismatchException;
import com.project.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final String jwtSecret;
    private final UserRepository userRepository;
    private final Clock clock;

    public UserService(
            @Value("${jwt.secret}") String jwtSecret, UserRepository userRepository, Clock clock) {
        this.jwtSecret = jwtSecret;
        this.userRepository = userRepository;
        this.clock = clock;
    }

    public RegisteredUserInfo registerUser(String email, String password, String passwordCheck) {
        var isEqual = password.equals(passwordCheck);
        if (!isEqual) throw new PasswordMismatchException();

        String hashedPassword = passwordEncoder.encode(password);
        User user = new User(email, hashedPassword, clock);
        userRepository.create(user);

        AuthToken token = AuthToken.issue(user.getId(), jwtSecret, clock);

        return new RegisteredUserInfo(token.getToken(), user.getId());
    }

    public String loginUser(String userId) {
        if (userId == null) throw new NeedToSignupException();

        User user = userRepository.getById(userId);
        user.setLastLoginAt(LocalDateTime.now(clock));

        return userId;
    }

    public void setManager(String userId) {
        User user = userRepository.getById(userId);
        user.setIsManager(true);
    }

    public User getById(String userId) {
        return userRepository.getById(userId);
    }
}
