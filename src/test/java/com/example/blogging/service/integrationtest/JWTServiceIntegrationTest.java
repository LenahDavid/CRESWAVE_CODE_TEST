package com.example.blogging.service.integrationtest;

import com.example.blogging.service.JWTServiceImpl;
import com.example.blogging.service.JWTService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JWTServiceIntegrationTest {

    @Mock
    private JWTServiceImpl jwtTokenUtil;

    @InjectMocks
    private JWTServiceImpl jwtService;

    @Test
    public void testExtractUserName() {
        String token = "example_token";
        String username = "testuser";
        when(jwtTokenUtil.extractUserName(token)).thenReturn(username);

        assertEquals(username, jwtService.extractUserName(token));
    }

    @Test
    public void testGenerateToken() {
        UserDetails userDetails = new User("testuser", "password", null);
        String expectedToken = "generated_token";
        when(jwtTokenUtil.generateToken(userDetails)).thenReturn(expectedToken);

        assertEquals(expectedToken, jwtService.generateToken(userDetails));
    }
}

