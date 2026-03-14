package com.project.common;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class SignUpTicket {
    private static final long EXPIRY_MS = 1000 * 60 * 10; // 10 minutes
    private static final String EMAIL_CLAIM = "EMAIL";

    private final String token;
    private final String email;

    private SignUpTicket(String token, String email) {
        this.token = token;
        this.email = email;
    }

    public static SignUpTicket issue(String email, String secret) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        String token = Jwts.builder()
                .claim(EMAIL_CLAIM, email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRY_MS))
                .signWith(key)
                .compact();
        return new SignUpTicket(token, email);
    }

    public static SignUpTicket parse(String token, String secret) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        var claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        String email = claims.get(EMAIL_CLAIM, String.class);
        return new SignUpTicket(token, email);
    }

    public String getToken() {
        return token;
    }

    public String getEmail() {
        return email;
    }
}