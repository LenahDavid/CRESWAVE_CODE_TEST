package com.example.blogging.controller;

import com.example.blogging.controllers.AuthenticationController;
import com.example.blogging.dto.JWTAuthenticationResponse;
import com.example.blogging.dto.SignInRequest;
import com.example.blogging.dto.SignUpRequest;
import com.example.blogging.entity.User;
import com.example.blogging.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthenticationControllerTest {

    @Mock
    AuthenticationService authenticationService;

    @InjectMocks
    AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignup() {
        SignUpRequest signUpRequest = new SignUpRequest();
        User user = new User();

        when(authenticationService.signup(signUpRequest)).thenReturn(user);

        ResponseEntity<User> responseEntity = authenticationController.signup(signUpRequest);

        assertEquals(user, responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(authenticationService, times(1)).signup(signUpRequest);
    }

    @Test
    void testSignin() {
        SignInRequest signInRequest = new SignInRequest();
        String token = "dummyToken";
        JWTAuthenticationResponse jwtAuthenticationResponse = new JWTAuthenticationResponse();

        when(authenticationService.signin(signInRequest)).thenReturn(jwtAuthenticationResponse);

        ResponseEntity<JWTAuthenticationResponse> responseEntity = authenticationController.signin(signInRequest);

        assertEquals(jwtAuthenticationResponse, responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(authenticationService, times(1)).signin(signInRequest);
    }
}
