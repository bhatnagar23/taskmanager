package com.taskmanager.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // This reads jwt.secret from application.properties
    @Value("${jwt.secret}")
    private String secret;

    // This reads jwt.expiration from application.properties
    @Value("${jwt.expiration}")
    private long expiration;

    // Create a JWT token for a user
    public String generateToken(String email) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder()
                .setSubject(email)          // Store email in token
                .setIssuedAt(new Date())    // When token was created
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Expiry
                .signWith(key, SignatureAlgorithm.HS256) // Sign with secret
                .compact();
    }

    // Get email from token
    public String getEmailFromToken(String token) {
        Key key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Check if token is valid
    public boolean validateToken(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(secret.getBytes());
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}