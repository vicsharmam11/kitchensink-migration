package com.demo.kitchensink.utils;

import com.demo.kitchensink.model.User;
import com.demo.kitchensink.model.Role;
import com.demo.kitchensink.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AdminBootstrap implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminBootstrap(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Check if admin user exists
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123")); // Default password
            admin.setRoles(Set.of(Role.ROLE_ADMIN));
            userRepository.save(admin);
            System.out.println("Default admin user created: admin/admin123");
        }

        // Check if normal user exists
        if (userRepository.findByUsername("user").isEmpty()) {
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123")); // Default password
            user.setRoles(Set.of(Role.ROLE_USER));
            userRepository.save(user);
            System.out.println("Default user created: user/user123");
        }
    }
}
