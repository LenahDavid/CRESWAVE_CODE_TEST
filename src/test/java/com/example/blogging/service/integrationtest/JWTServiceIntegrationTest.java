package com.example.blogging.service.integrationtest;

import com.example.blogging.entity.User;
import com.example.blogging.service.JWTServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

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
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("testuser")
                .password("password")
                .authorities(Collections.singletonList(() -> "ROLE_USER"))
                .build();

        String expectedToken = "generated_token";
        when(jwtTokenUtil.generateToken(userDetails)).thenReturn(expectedToken);

        String actualToken = jwtService.generateToken(userDetails);
        System.out.println("Actual token: " + actualToken);

        assertEquals(expectedToken, actualToken);
    }

}

