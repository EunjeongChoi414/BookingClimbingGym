package com.project.services.user;

import com.project.common.AuthToken;
import com.project.domain.exception.NeedToLoginAgainException;
import com.project.domain.exception.NeedToSignupException;
import com.project.domain.exception.PasswordMismatchException;
import com.project.domain.user.User;
import com.project.domain.user.UserRepository;
import com.project.services.user.model.RegisteredUserInfo;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

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

    public RegisteredUserInfo registerUser(
            String email, String password, String passwordCheck) {
        var isEqual = password.equals(passwordCheck);
        if (!isEqual) throw new PasswordMismatchException();

        String hashedPassword = passwordEncoder.encode(password);
        User user = new User(email, hashedPassword, clock);
        userRepository.create(user);

        AuthToken token = AuthToken.issue(user.getId(), jwtSecret, clock);

        return new RegisteredUserInfo(token.getToken(), user.getId());
    }

    public String loginUser(String token) {
        if (token == null) throw new NeedToSignupException();
        AuthToken parsedToken;
        try {
            parsedToken = AuthToken.parse(token, jwtSecret);
        } catch (ExpiredJwtException e) {
            throw new NeedToLoginAgainException();
        }

        String userId = parsedToken.getUserId();
        User user = userRepository.findById(userId);
        user.setLastLoginAt(LocalDateTime.now(clock));

        return userId;
    }

    public String getUserIdFromToken(String token) {
        AuthToken parsedToken = AuthToken.parse(token, jwtSecret);
        return parsedToken.getUserId();
    }
}