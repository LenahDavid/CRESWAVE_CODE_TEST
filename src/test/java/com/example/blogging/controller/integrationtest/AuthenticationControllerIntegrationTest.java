package com.example.blogging.controller.integrationtest;

import com.example.blogging.controllers.AuthenticationController;
import com.example.blogging.dto.JWTAuthenticationResponse;
import com.example.blogging.dto.SignInRequest;
import com.example.blogging.dto.SignUpRequest;
import com.example.blogging.entity.User;
import com.example.blogging.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthenticationController.class) // Focus on web layer
@ExtendWith({MockitoExtension.class, SpringExtension.class}) // Enable extensions
public class AuthenticationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // Mock the service
    private AuthenticationService authenticationService;


    @Test
    public void testSignup() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest();
        // Set up your signUpRequest object

        User user = new User();
        // Set up your User object

        when(authenticationService.signup(any(SignUpRequest.class))).thenReturn(user);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(signUpRequest);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        // Add more assertions if needed
    }

    @Test
    public void testSignin() throws Exception {
        SignInRequest signInRequest = new SignInRequest();
        // Set up your signInRequest object

        JWTAuthenticationResponse jwtAuthenticationResponse = new JWTAuthenticationResponse();
        // Set up your JWTAuthenticationResponse object

        when(authenticationService.signin(any(SignInRequest.class))).thenReturn(jwtAuthenticationResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(signInRequest);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

    }
}
