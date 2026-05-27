package com.acrobtw.elei.security.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

import com.acrobtw.elei.core.security.jwt.JwtService;
import com.acrobtw.elei.domain.user.User;

import io.jsonwebtoken.ExpiredJwtException;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {


    private JwtService jwtService;

    private User testUser;

    @BeforeEach
    void setUp() {
        String testSecret = "94a08da1fecbb6e8b46990538c7b50b2715f8028d7285a3a2d21e861d85603de";
        jwtService = new JwtService(testSecret);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 86400000L);

        testUser = new User();
        testUser.setId(100L);
        testUser.setUsername("test_user");
    }

    @Test
    void shouldGenerateToken() {
        String token = jwtService.generateToken(testUser);

        assertNotNull(token);
        assertEquals(testUser.getUsername(),jwtService.extractUsername(token));

        Long userId = jwtService.extractClaim(token, claims -> claims.get("userId", Long.class));
        assertEquals(testUser.getId(), userId);
    }

    @Test
    void shouldThrowExceptionWhenTokenIsExpired() {
        JwtService expiredJwtService = new JwtService("94a08da1fecbb6e8b46990538c7b50b2715f8028d7285a3a2d21e861d85603de");
        ReflectionTestUtils.setField(expiredJwtService, "jwtExpiration", 0L);

        String expiredToken = expiredJwtService.generateToken(testUser);
        assertThrows(ExpiredJwtException.class, () -> {
        expiredJwtService.isTokenValid(expiredToken, testUser);
    });

    }
}
