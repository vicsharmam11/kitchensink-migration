package com.demo.kitchensink.security;

import com.demo.kitchensink.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    @Mock
    JwtUtil jwtUtil;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    FilterChain filterChain;

    @InjectMocks
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldAuthenticateRequest_whenValidJwtTokenIsPresent() throws Exception {
        String token = "valid-token";
        Claims claims = mock(Claims.class);

        when(request.getHeader("Authorization")).thenReturn("bearer " + token);
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.extractAllClaims(token)).thenReturn(claims);
        when(claims.getSubject()).thenReturn("admin");
        when(claims.get("role", String.class)).thenReturn("ADMIN");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateRequest_whenInvalidJwtTokenIsPresent() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("bearer invalid-token");
        when(jwtUtil.validateToken(anyString())).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }
}
