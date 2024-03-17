package com.example.blogging.service.integrationtest;

import com.example.blogging.dto.JWTAuthenticationResponse;
import com.example.blogging.dto.SignInRequest;
import com.example.blogging.dto.SignUpRequest;
import com.example.blogging.entity.Role;
import com.example.blogging.entity.User;
import com.example.blogging.repository.UserRepository;
import com.example.blogging.service.AuthenticationService;
import com.example.blogging.service.AuthenticationServiceImpl;
import com.example.blogging.service.JWTService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceIntegrationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private AuthenticationService authenticationService = new AuthenticationServiceImpl(userRepository, passwordEncoder, authenticationManager, jwtService);

    @Test
    public void testSignup() {
        // Given
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("testuser");
        signUpRequest.setPassword("password");

        when(passwordEncoder.encode(signUpRequest.getPassword())).thenReturn("encodedPassword");

        // When
        User savedUser = authenticationService.signup(signUpRequest);

        // Then
        assertNotNull(savedUser);
        assertEquals("testuser", savedUser.getUsername());
        assertEquals(Role.USER, savedUser.getRole());
        assertEquals("encodedPassword", savedUser.getPassword());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testSignin() {
        // Given
        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setUsername("testuser");
        signInRequest.setPassword("password");

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername(signInRequest.getUsername())).thenReturn(Optional.of(user));

        String jwtToken = "jwtToken";
        String refreshToken = "refreshToken";
        when(jwtService.generateToken(user)).thenReturn(jwtToken);
        when(jwtService.generateRefreshToken(any(), any(User.class))).thenReturn(refreshToken);

        // When
        JWTAuthenticationResponse response = authenticationService.signin(signInRequest);

        // Then
        assertNotNull(response);
        assertEquals(jwtToken, response.getToken());
        assertEquals(refreshToken, response.getRefreshToken());
    }
}

