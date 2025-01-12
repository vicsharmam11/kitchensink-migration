package com.demo.kitchensink.controller;

import com.demo.kitchensink.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private JwtUtil jwtUtil;

    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptions() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody HashMap<String,String> user) {
        String username= user.get("username");
        String password=user.get("password");
        // Dummy authentication for demonstration purposes
        if ("admin".equals(username) && "admin123".equals(password)) {
            String token = jwtUtil.generateToken(username, "ADMIN");
            return ResponseEntity.ok(Map.of("Authorization","bearer "+ token));
        } else if ("user".equals(username) && "user123".equals(password)) {
            String token = jwtUtil.generateToken(username, "USER");
            return ResponseEntity.ok(Map.of("Authorization", "bearer "+token));
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}

