package com.acrobtw.elei.service;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;

import java.util.Date;
import java.util.Map;

import com.acrobtw.elei.entity.User;

import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final SecretKey key;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public JwtService(@Value("${jwt.secret}") String key) {
        this.key = Keys.hmacShaKeyFor(key.getBytes());
    }

    public String generateToken(User user) {
        Map<String, Object> extraClaims = Map.of("userId", user.getId());

        return Jwts.builder()
        .claims(extraClaims)
        .subject(user.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
        .signWith(key)
        .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
    }
}
