package com.demo.kitchensink.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService {

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(
                User.withUsername("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .roles("ADMIN")
                        .build()
        );
        manager.createUser(
                User.withUsername("user")
                        .password(passwordEncoder.encode("user123"))
                        .roles("USER")
                        .build()
        );
        return manager;
    }
}

