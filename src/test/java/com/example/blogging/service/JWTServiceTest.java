package com.example.blogging.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class JWTServiceTest {

    @InjectMocks
    private JWTService jwtService = new JWTService() {
        @Override
        public String extractUserName(String token) {
            // Mock implementation, just return a fixed value
            return "testUser";
        }

        @Override
        public String generateToken(UserDetails userDetails) {
            // Mock implementation, just return a fixed token
            return "mockedToken";
        }

        @Override
        public boolean isTokenValid(String token, UserDetails userDetails) {
            // Mock implementation, just return true
            return true;
        }

        @Override
        public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails) {
            // Mock implementation, just return a fixed token
            return "mockedRefreshToken";
        }
    };

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Collection<? extends GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));

        userDetails = User.builder()
                .username("testUser")
                .password("password")
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    @Test
    void testExtractUserName() {
        String extractedUserName = jwtService.extractUserName("mockedToken");
        Assertions.assertEquals("testUser", extractedUserName);
    }

    @Test
    void testGenerateToken() {
        String generatedToken = jwtService.generateToken(userDetails);
        Assertions.assertEquals("mockedToken", generatedToken);
    }

    @Test
    void testIsTokenValid() {
        boolean isValid = jwtService.isTokenValid("mockedToken", userDetails);
        Assertions.assertTrue(isValid);
    }

    @Test
    void testGenerateRefreshToken() {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("customClaim", "customValue");

        String refreshToken = jwtService.generateRefreshToken(extraClaims, userDetails);
        Assertions.assertEquals("mockedRefreshToken", refreshToken);
    }
}
