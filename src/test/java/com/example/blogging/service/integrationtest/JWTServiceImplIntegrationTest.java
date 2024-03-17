package com.example.blogging.service.integrationtest;


import com.example.blogging.service.JWTServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JWTServiceImplIntegrationTest {

    @InjectMocks
    private JWTServiceImpl jwtService;
    JWTServiceImpl jwtTokenUtil;

    @Test
    public void testGenerateToken() {
        // Create a mock User with a single authority
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("testuser")
                .password("password")
                .authorities(Collections.singletonList(() -> "ROLE_USER"))
                .build();

        String expectedToken = "generated_token";
        when(jwtTokenUtil.generateToken(userDetails)).thenReturn(expectedToken);

        assertEquals(expectedToken, jwtService.generateToken(userDetails));
    }

    @Test
    public void testGenerateRefreshToken() {
        // Given
        UserDetails userDetails = new User("testuser", "password", null);
        Map<String, Object> extraClaims = new HashMap<>();

        // When
        String refreshToken = jwtService.generateRefreshToken(extraClaims, userDetails);

        // Then
        assertNotNull(refreshToken);
        assertTrue(refreshToken.length() > 0);
    }

    @Test
    public void testExtractUserName() {
        // Given
        String token = "example_token";

        // When
        String username = jwtService.extractUserName(token);

        // Then
        assertNull(username); // Since this is a mock test, we are not parsing the token
    }

    // You can similarly test other methods such as isTokenValid and other private helper methods
}
