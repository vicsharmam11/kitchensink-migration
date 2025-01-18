package com.demo.kitchensink.utils;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtUtilTest {

    JwtUtil jwtUtil = new JwtUtil();

    @Test
    public void testGenerateToken() {
        String username = "testUser";
        String role = "USER";

        String token = jwtUtil.generateToken(username, role);

        assertNotNull(token);
        assertTrue(token.startsWith("eyJ"));
    }

    @Test
    public void testValidateTokenValid() {
        String username = "testUser";
        String role = "USER";
        String token = jwtUtil.generateToken(username, role);

        boolean isValid = jwtUtil.validateToken(token);

        assertTrue(isValid);
    }

    @Test
    public void testValidateTokenInvalid() {
        String invalidToken = "invalidToken";

        boolean isValid = jwtUtil.validateToken(invalidToken);

        assertFalse(isValid);
    }

    @Test
    public void testExtractUsername() {
        String username = "testUser";
        String token = jwtUtil.generateToken(username, "USER");

        String extractedUsername = jwtUtil.extractUsername(token);

        assertEquals(username, extractedUsername);
    }

    @Test
    public void testExtractRole() {
        String role = "ADMIN";
        String token = jwtUtil.generateToken("testUser", role);

        String extractedRole = jwtUtil.extractRole(token);

        assertEquals(role, extractedRole);
    }

    @Test
    public void testTokenExpiration() {
        String token = jwtUtil.generateToken("testUser", "USER");

        Claims claims = jwtUtil.extractAllClaims(token);
        Date expiration = claims.getExpiration();

        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    public void testInvalidTokenParsing() {
        String invalidToken = "invalidToken";

        try {
            jwtUtil.extractAllClaims(invalidToken);
            fail("Expected an exception to be thrown");
        } catch (Exception e) {
            assertTrue(e instanceof io.jsonwebtoken.MalformedJwtException);
        }
    }

    @Test
    public void testInvalidSecretKeyValidation() {
        String token = jwtUtil.generateToken("testUser", "USER");
        String invalidSecretKey = "InvalidSecretKey";

        try {
            io.jsonwebtoken.Jwts.parser().setSigningKey(invalidSecretKey).parseClaimsJws(token);
            fail("Expected an exception to be thrown");
        } catch (Exception e) {
            assertTrue(e instanceof io.jsonwebtoken.SignatureException);
        }
    }
}
