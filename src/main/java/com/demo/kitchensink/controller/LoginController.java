package com.demo.kitchensink.controller;

import com.demo.kitchensink.model.User;
import com.demo.kitchensink.repository.UserRepository;
import com.demo.kitchensink.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptions() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody HashMap<String, String> user) {
        String username = user.get("username");
        String password = user.get("password");
        Optional<User> userAdmin = userRepository.findByUsername("admin");
        Optional<User> userBasic = userRepository.findByUsername("user");
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();

        if (userAdmin.isPresent() && userAdmin.get().getUsername().equals(username) && encoder.matches(password,userAdmin.get().getPassword())) {
            String token = jwtUtil.generateToken(username, "ADMIN");
            return ResponseEntity.ok(Map.of("Authorization", "bearer " + token));
        } else if (userBasic.isPresent() && userBasic.get().getUsername().equals(username) && encoder.matches(password,userBasic.get().getPassword())) {
            String token = jwtUtil.generateToken(username, "USER");
            return ResponseEntity.ok(Map.of("Authorization", "bearer " + token));
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}

