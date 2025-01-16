package com.demo.kitchensink.controller;

import com.demo.kitchensink.exception.CustomException;
import com.demo.kitchensink.model.Member;
import com.demo.kitchensink.repository.MemberRepository;
import com.demo.kitchensink.utils.JwtUtil;
import com.demo.kitchensink.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class MemberControllerTest {


    @Mock
    MemberRepository memberRepository;

    @Mock
    JwtUtil jwtUtils;

    @Mock
    MemberService memberService;


    @InjectMocks
    private MemberController memberController;

    Member validMember;
    String adminJwtToken;
    String userJwtToken;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Sample member
        validMember=new Member(1, "John Doe", "john.doe@example.com", "7778889996" );

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
        when(memberService.createMember(any())).thenReturn(validMember);

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
        when(memberService.getMemberById(1)).thenReturn(Optional.of(validMember));

        // Act
        ResponseEntity<Member> response = memberController.getMemberById("1");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(validMember, response.getBody());
        verify(memberService, times(1)).getMemberById(1);
    }

    @Test
    void getMemberById_shouldReturn404_whenMemberDoesNotExist() {

        doThrow(new CustomException("2","Invalid Id provided")).when(memberService).getMemberById(any());

        assertThrows(CustomException.class, () -> memberController.getMemberById("2"));
    }

    @Test
    void getAllMembers() {
        // Arrange
        List<Member> memberList=new ArrayList<>();
        memberList.add(validMember);
        when(memberService.getAllMembers()).thenReturn(memberList);

        // Act
        ResponseEntity<List<Member>> response = memberController.getAllMembers();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(memberService, times(1)).getAllMembers();
    }

    @Test
    void deleteMember_shouldReturn204() {
        // Arrange
        doNothing().when(memberService).deleteMember(1);

        // Act
        ResponseEntity<Void> response = memberController.deleteMember("1");

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(memberService, times(1)).deleteMember(1);
    }

    @Test
    void updateMember_shouldReturn200() {
        // Arrange
        validMember.setName("Updated Name");
        when(jwtUtils.extractRole("admin-jwt-token")).thenReturn("ADMIN");
        when(memberService.getMemberById(any())).thenReturn(Optional.ofNullable(validMember));

        when(memberService.updateMember(1, validMember)).thenReturn(validMember);

        ResponseEntity<Member> response = memberController.updateMember("1",  validMember);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(validMember, response.getBody());
        verify(memberService, times(1)).updateMember(1, validMember);
    }

    @Test
    void updateMember_shouldReturn404_whenMemberDoesNotExist() {
        // Arrange
        validMember.setName("Updated Name");
        when(jwtUtils.extractRole("admin-jwt-token")).thenReturn("ADMIN");

        doThrow(new CustomException("1","Invalid Id provided")).when(memberService).getMemberById(any());

        // Assert
        assertThrows(CustomException.class, () -> memberController.updateMember("1", validMember));    }
}
