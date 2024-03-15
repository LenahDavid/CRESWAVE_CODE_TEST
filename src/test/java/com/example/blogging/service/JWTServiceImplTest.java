package com.example.blogging.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class JWTServiceImplTest {

    @InjectMocks
    private JWTServiceImpl jwtService;

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userDetails = new User(
                "testUser",
                "password",
                Collections.emptyList());
    }

    @Test
    void testGenerateToken() {
        String generatedToken = jwtService.generateToken(userDetails);
        Assertions.assertNotNull(generatedToken);
    }

    @Test
    void testGenerateRefreshToken() {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("customClaim", "customValue");

        String refreshToken = jwtService.generateRefreshToken(extraClaims, userDetails);
        Assertions.assertNotNull(refreshToken);
    }

    @Test
    void testExtractUserName() {
        String token = jwtService.generateToken(userDetails);
        String extractedUserName = jwtService.extractUserName(token);
        Assertions.assertEquals("testUser", extractedUserName);
    }

    @Test
    void testIsTokenValid() {
        String token = jwtService.generateToken(userDetails);
        boolean isValid = jwtService.isTokenValid(token, userDetails);
        Assertions.assertTrue(isValid);
    }
}

