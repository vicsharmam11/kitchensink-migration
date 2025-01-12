package com.demo.kitchensink.controller;

import com.demo.kitchensink.model.Member;
import com.demo.kitchensink.utils.JwtUtil;
import com.demo.kitchensink.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MemberControllerTest {

    @Mock
    private MemberService memberService;

    @Mock
    private JwtUtil jwtUtils;

    @InjectMocks
    private MemberController memberController;
    @Mock
    private Member validMember;
    private String adminJwtToken;
    private String userJwtToken;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Sample member

        validMember.setId("1");
        validMember.setName("John Doe");
        validMember.setEmail("john.doe@example.com");

        // Sample JWT tokens
        adminJwtToken = "bearer admin-jwt-token";
        userJwtToken = "bearer user-jwt-token";

        // Mock JWT roles
        when(jwtUtils.extractRole("admin-jwt-token")).thenReturn("ADMIN");
        when(jwtUtils.extractRole("user-jwt-token")).thenReturn("USER");
    }

    @Test
    void createMember_shouldReturn201() {
        // Arrange
        when(jwtUtils.extractRole("admin-jwt-token")).thenReturn("ADMIN");
        when(memberService.createMember(validMember)).thenReturn(validMember);

        // Act
        ResponseEntity<Member> response = memberController.createMember(validMember);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(validMember, response.getBody());
        verify(memberService, times(1)).createMember(validMember);
    }

    @Test
    void getMemberById_shouldReturnMember_whenMemberExists() {
        // Arrange
        when(memberService.getMemberById("1")).thenReturn(Optional.of(validMember));

        // Act
        ResponseEntity<Member> response = memberController.getMemberById("1");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(validMember, response.getBody());
        verify(memberService, times(1)).getMemberById("1");
    }

    @Test
    void getMemberById_shouldReturn404_whenMemberDoesNotExist() {
        // Arrange
        when(memberService.getMemberById("1")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Member> response = memberController.getMemberById("1");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(memberService, times(1)).getMemberById("1");
    }

    @Test
    void deleteMember_shouldReturn204() {
        // Arrange
        doNothing().when(memberService).deleteMember("1");

        // Act
        ResponseEntity<Void> response = memberController.deleteMember("1");

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(memberService, times(1)).deleteMember("1");
    }

    @Test
    void updateMember_shouldReturn200() {
        // Arrange
        validMember.setName("Updated Name");
        when(jwtUtils.extractRole("admin-jwt-token")).thenReturn("ADMIN");
        when(memberService.updateMember("1", validMember)).thenReturn(validMember);

        // Act
        ResponseEntity<Member> response = memberController.updateMember("1",  validMember);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(validMember, response.getBody());
        verify(memberService, times(1)).updateMember("1", validMember);
    }

    @Test
    void updateMember_shouldReturn404_whenMemberDoesNotExist() {
        // Arrange
        validMember.setName("Updated Name");
        when(jwtUtils.extractRole("admin-jwt-token")).thenReturn("ADMIN");
        when(memberService.updateMember("1", validMember)).thenReturn(null);

        // Act
        ResponseEntity<Member> response = memberController.updateMember("1", validMember);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(memberService, times(1)).updateMember("1", validMember);
    }
}
