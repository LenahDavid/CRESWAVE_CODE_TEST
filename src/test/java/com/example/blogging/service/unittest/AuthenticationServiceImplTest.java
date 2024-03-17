package com.example.blogging.service.unittest;

import com.example.blogging.dto.JWTAuthenticationResponse;
import com.example.blogging.dto.SignInRequest;
import com.example.blogging.dto.SignUpRequest;
import com.example.blogging.entity.Role;
import com.example.blogging.entity.User;
import com.example.blogging.repository.UserRepository;
import com.example.blogging.service.AuthenticationServiceImpl;
import com.example.blogging.service.JWTService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Optional;

class AuthenticationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private SignUpRequest signUpRequest;
    private SignInRequest signInRequest;
    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("testUser");
        signUpRequest.setPassword("password");

        signInRequest = new SignInRequest();
        signInRequest.setUsername("testUser");
        signInRequest.setPassword("password");

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");
        testUser.setRole(Role.USER);
        testUser.setPassword("hashedPassword"); // Use a hashed password
    }

    @Test
    void testSignup() {
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(testUser);
        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("hashedPassword");

        User createdUser = authenticationService.signup(signUpRequest);

        Assertions.assertEquals(testUser, createdUser);
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }

    @Test
    void testSignin_Success() {
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(testUser));
        Mockito.when(jwtService.generateToken(Mockito.any(User.class))).thenReturn("jwtToken");
        Mockito.when(jwtService.generateRefreshToken(Mockito.anyMap(), Mockito.any(User.class))).thenReturn("refreshToken");

        JWTAuthenticationResponse jwtAuthenticationResponse = authenticationService.signin(signInRequest);

        Assertions.assertNotNull(jwtAuthenticationResponse.getToken());
        Assertions.assertNotNull(jwtAuthenticationResponse.getRefreshToken());
    }

    @Test
    void testSignin_InvalidCredentials() {
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.empty());
        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenThrow(new BadCredentialsException("Invalid credentials"));

        Assertions.assertThrows(BadCredentialsException.class, () -> authenticationService.signin(signInRequest));
    }
}

