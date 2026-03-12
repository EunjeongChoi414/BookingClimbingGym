package com.project.services.user;

import com.project.common.AuthToken;
import com.project.domain.user.User;
import com.project.domain.user.UserRepository;
import com.project.services.user.model.RegisteredUserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final String jwtSecret;
    private final UserRepository userRepository;

    public UserService(@Value("${jwt.secret}") String jwtSecret, UserRepository userRepository) {
        this.jwtSecret = jwtSecret;
        this.userRepository = userRepository;
    }

    public RegisteredUserInfo registerUser(String email, String password) {
        String hashedPassword = passwordEncoder.encode(password);
        User user = new User(email, hashedPassword);
        userRepository.create(user);

        AuthToken token = AuthToken.issue(user.getId(), jwtSecret);
        return new RegisteredUserInfo(token.getToken(), user.getId());
    }

    public String tryLoginUser(String token) {
        AuthToken parsedToken = AuthToken.parse(token, jwtSecret);
        String userId = parsedToken.getUserId();
        userRepository.findById(userId).ifPresent(user -> {
            user.setLastLoginAt(LocalDateTime.now());
        });
        return userId;
    }
}