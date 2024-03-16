package com.example.blogging.service;

import com.example.blogging.dto.JWTAuthenticationResponse;
import com.example.blogging.dto.SignInRequest;
import com.example.blogging.dto.SignUpRequest;
import com.example.blogging.entity.Role;
import com.example.blogging.entity.User;
import com.example.blogging.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthenticationServiceImplIntegrationTest {

    @Autowired
    private AuthenticationService authenticationService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JWTService jwtService;

    @Test
    public void testSignup() {
        // Given
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("testuser");
        signUpRequest.setPassword("testpassword");

        User savedUser = new User();
        savedUser.setUsername("testuser");
        savedUser.setRole(Role.USER);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        User user = authenticationService.signup(signUpRequest);

        // Then
        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
        assertEquals(Role.USER, user.getRole());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testSignin() {
        // Given
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setUsername("testuser");
        signInRequest.setPassword("testpassword");

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedpassword");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);

        HashMap<String, Object> claims = new HashMap<>();
        String token = "jwt_token";
        String refreshToken = "refresh_token";
        when(jwtService.generateToken(any(User.class))).thenReturn(token);
        when(jwtService.generateRefreshToken(anyMap(), any(User.class))).thenReturn(refreshToken);

        // When
        JWTAuthenticationResponse response = authenticationService.signin(signInRequest);

        // Then
        assertNotNull(response);
        assertEquals(token, response.getToken());
        assertEquals(refreshToken, response.getRefreshToken());
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(jwtService, times(1)).generateToken(any(User.class));
        verify(jwtService, times(1)).generateRefreshToken(anyMap(), any(User.class));
    }
}
