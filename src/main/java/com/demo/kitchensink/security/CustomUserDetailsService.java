package com.demo.kitchensink.security;

import com.demo.kitchensink.model.User;
import com.demo.kitchensink.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService{

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return CustomUserDetails.build(user);
    }

/*    @Bean
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
    }*/

}

