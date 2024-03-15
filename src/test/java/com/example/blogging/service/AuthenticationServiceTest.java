package com.example.blogging.service;

import com.example.blogging.dto.JWTAuthenticationResponse;
import com.example.blogging.dto.SignInRequest;
import com.example.blogging.dto.SignUpRequest;
import com.example.blogging.entity.User;
import com.example.blogging.repository.UserRepository;
import com.example.blogging.service.AuthenticationServiceImpl;
import com.example.blogging.service.JWTService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTService jwtService;

    @Mock
    private AuthenticationManager authenticationManager; // Add mocked AuthenticationManager

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private SignUpRequest signUpRequest;
    private SignInRequest signInRequest;
    private User testUser;

    @BeforeEach
    void setUp() {
        signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("testUser");
        signUpRequest.setPassword("password");

        signInRequest = new SignInRequest();
        signInRequest.setUsername("testUser");
        signInRequest.setPassword("password");

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");
        // You can set other fields as needed
    }

    @Test
    void testSignup() {
        Mockito.when(userRepository.save(any(User.class))).thenReturn(testUser);

        User createdUser = authenticationService.signup(signUpRequest);

        Assertions.assertEquals(testUser, createdUser);
    }

    @Test
    void testSignin() {
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(java.util.Optional.of(testUser));
        Mockito.when(jwtService.generateToken(Mockito.any())).thenReturn("mockedToken"); // Mock the token generation

        JWTAuthenticationResponse jwtAuthenticationResponse = authenticationService.signin(signInRequest);

        Assertions.assertNotNull(jwtAuthenticationResponse.getToken());
    }

}
