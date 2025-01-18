package com.demo.kitchensink.security;

import com.demo.kitchensink.model.Role;
import com.demo.kitchensink.model.User;
import com.demo.kitchensink.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CustomUserDetailsServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final CustomUserDetailsService userDetailsService = new CustomUserDetailsService(userRepository);

    @Test
    public void testLoadUserByUsername_Success() {
        String username = "testUser";
        User mockUser = new User();
        mockUser.setId("1");
        mockUser.setUsername(username);
        mockUser.setPassword("password123");
        mockUser.setRoles(Set.of(Role.ROLE_USER));

        when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.of(mockUser));

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        assertEquals(username, userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        assertEquals(Role.ROLE_USER.toString(), userDetails.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        String username = "nonExistentUser";

        when(userRepository.findByUsername(anyString())).thenReturn(java.util.Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(username);
        });
    }

    // Add more tests if needed
}
