package com.demo.kitchensink.utils;

import com.demo.kitchensink.model.Role;
import com.demo.kitchensink.model.User;
import com.demo.kitchensink.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;

class AdminBootstrapTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    AdminBootstrap adminBootstrap;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateDefaultAdminAndUser_whenNotExisting() {
        when(userRepository.findByUsername("admin")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("user")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encoded-password");

        adminBootstrap.run();

        verify(userRepository, times(1)).save(argThat(user -> user.getUsername().equals("admin") && user.getRoles().contains(Role.ROLE_ADMIN)));
        verify(userRepository, times(1)).save(argThat(user -> user.getUsername().equals("user") && user.getRoles().contains(Role.ROLE_USER)));
    }

    @Test
    void shouldNotCreateDefaultAdminAndUser_whenExisting() {
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(new User()));
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(new User()));

        adminBootstrap.run();

        verify(userRepository, never()).save(any());
    }
}
