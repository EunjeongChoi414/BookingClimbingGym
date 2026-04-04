package com.project.common.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.util.Date;

public class AuthToken {
    private static final long EXPIRY_MS = 1000 * 60 * 60 * 24 * 30L; // 30 days
    private static final String USER_ID_CLAIM = "userId";

    private final String token;
    private final String userId;

    private AuthToken(String token, String userId) {
        this.token = token;
        this.userId = userId;
    }

    public static AuthToken issue(String userId, String secret, Clock clock) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        String token = Jwts.builder()
                .claim(USER_ID_CLAIM, userId)
                .issuedAt(new Date(clock.millis()))
                .expiration(new Date(clock.millis() + EXPIRY_MS))
                .signWith(key)
                .compact();
        return new AuthToken(token, userId);
    }

    public static AuthToken parse(String token, String secret) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        var claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        String userId = claims.get(USER_ID_CLAIM, String.class);
        return new AuthToken(token, userId);
    }

    public String getToken() {
        return token;
    }

    public String getUserId() {
        return userId;
    }
}
