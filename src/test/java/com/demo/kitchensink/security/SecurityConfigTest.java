package com.demo.kitchensink.security;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SecurityConfigTest {

    @Mock
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    CustomUserDetailsService customUserDetailsService;

    @Test
    void shouldReturnPasswordEncoder() {
        SecurityConfig securityConfig = new SecurityConfig(customUserDetailsService);
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        assertNotNull(passwordEncoder);
        assert passwordEncoder instanceof BCryptPasswordEncoder;
    }
}
