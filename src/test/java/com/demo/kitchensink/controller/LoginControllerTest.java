package com.demo.kitchensink.controller;


import com.demo.kitchensink.model.User;
import com.demo.kitchensink.repository.UserRepository;
import com.demo.kitchensink.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LoginControllerTest {

    @Mock
    private JwtUtil jwtUtils;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_shouldReturnJwtToken_whenCredentialsAreValid() {
        // Arrange
        String username = "admin";
        String password = "admin123";
        String role = "ADMIN";
        String jwtToken = "mocked-jwt-token";
        User user=new User();
        user.setUsername("admin");
        BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
        String encodedPass=bCryptPasswordEncoder.encode(password);
        user.setPassword(encodedPass);
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));


        HashMap<String, String> credentials = new HashMap<>() {{ put("username", username); put("password", password); }};

        // Mock the JWT generation
        when(jwtUtils.generateToken(username, role)).thenReturn(jwtToken);

        // Act
        ResponseEntity<Map<String, String>> response = (ResponseEntity<Map<String, String>>) loginController.login(credentials);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(jwtToken, response.getBody().get("Authorization").replace("bearer ",""));
        verify(jwtUtils, times(1)).generateToken(username, role);
    }

    @Test
    void login_shouldThrowException_whenCredentialsAreInvalid() {
        User user=new User();
        user.setUsername("admin");
        user.setPassword("admin");
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        // Arrange
        String username = "admin";
        String password = "password";

        HashMap<String, String> credentials = new HashMap<>() {{ put("username", username); put("password", password); }};

        // No mocking for JWT generation as this is invalid

        // Act
        try {
            loginController.login(credentials);
        } catch (RuntimeException ex) {
            // Assert
            assertEquals("Invalid credentials", ex.getMessage());
        }

        verify(jwtUtils, never()).generateToken(anyString(), anyString());
    }
}

