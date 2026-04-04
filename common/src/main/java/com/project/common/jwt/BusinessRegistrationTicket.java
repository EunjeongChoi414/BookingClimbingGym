package com.project.common.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class BusinessRegistrationTicket {
    private static final long EXPIRY_MS = 1000 * 60 * 10; // 10 minutes
    private static final String VERIFIED_BUSINESS_CLAIM = "VERIFIED_BUSINESS_CLAIM";

    private final String token;
    private final String representativeName;

    private BusinessRegistrationTicket(String token, String representativeName) {
        this.token = token;
        this.representativeName = representativeName;
    }

    public static BusinessRegistrationTicket issue(String representativeName, String secret) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        String token = Jwts.builder()
                .claim(VERIFIED_BUSINESS_CLAIM, representativeName)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRY_MS))
                .signWith(key)
                .compact();
        return new BusinessRegistrationTicket(token, representativeName);
    }

    public static BusinessRegistrationTicket parse(String token, String secret) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        var claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        String representativeName = claims.get(VERIFIED_BUSINESS_CLAIM, String.class);
        return new BusinessRegistrationTicket(token, representativeName);
    }

    public String getToken() {
        return token;
    }

    public String getRepresentativeName() {
        return representativeName;
    }
}
