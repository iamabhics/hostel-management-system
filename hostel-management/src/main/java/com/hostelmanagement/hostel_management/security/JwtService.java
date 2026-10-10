
package com.hostelmanagement.hostel_management.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long expiration;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration}") long expiration) {

       
    if (secret == null || secret.trim().isEmpty()) {
        throw new IllegalArgumentException(
                "JWT secret must not be empty"
        );
    }

    if (expiration <= 0) {
        throw new IllegalArgumentException(
                "JWT expiration must be greater than zero"
        );
    }

    this.secretKey = Keys.hmacShaKeyFor(
            secret.getBytes(StandardCharsets.UTF_8)
    );

    this.expiration = expiration;
    }

    public String generateToken(String email, String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}