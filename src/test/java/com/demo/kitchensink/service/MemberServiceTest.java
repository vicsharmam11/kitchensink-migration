package com.demo.kitchensink.service;

import com.demo.kitchensink.model.Member;
import com.demo.kitchensink.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;

    @Mock
    SequenceGeneratorService sequenceGenerator;

    @InjectMocks
    MemberService memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUsersByName() {
        String name = "John";
        List<Member> members = Arrays.asList(new Member(1, "John", "john@example.com", "1234567890"));

        when(memberRepository.findByName(name)).thenReturn(members);

        List<Member> result = memberService.getUsersByName(name);

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getName());
        verify(memberRepository, times(1)).findByName(name);
    }

    @Test
    void testSearchUsersByName() {
        String name = "jo";
        List<Member> members = Arrays.asList(new Member(1, "John", "john@example.com", "1234567890"));

        when(memberRepository.findByNameContainingIgnoreCase(name)).thenReturn(members);

        List<Member> result = memberService.searchUsersByName(name);

        assertEquals(1, result.size());
        assertTrue(result.get(0).getName().contains("John"));
        verify(memberRepository, times(1)).findByNameContainingIgnoreCase(name);
    }

    @Test
    void testGetUsersByPhonePrefix() {
        String prefix = "123";
        List<Member> members = Arrays.asList(new Member(1, "John", "john@example.com", "1234567890"));

        when(memberRepository.findByPhoneStartingWith(prefix)).thenReturn(members);

        List<Member> result = memberService.getUsersByPhonePrefix(prefix);

        assertEquals(1, result.size());
        assertTrue(result.get(0).getPhone().startsWith(prefix));
        verify(memberRepository, times(1)).findByPhoneStartingWith(prefix);
    }

    @Test
    void testDoesUserExistByEmail() {
        String email = "john@example.com";

        when(memberRepository.existsByEmail(email)).thenReturn(true);

        boolean result = memberService.doesUserExistByEmail(email);

        assertTrue(result);
        verify(memberRepository, times(1)).existsByEmail(email);
    }

    @Test
    void testGetAllMembers() {
        List<Member> members = Arrays.asList(new Member(1, "John", "john@example.com", "1234567890"));

        when(memberRepository.findAll()).thenReturn(members);

        List<Member> result = memberService.getAllMembers();

        assertEquals(1, result.size());
        verify(memberRepository, times(1)).findAll();
    }

    @Test
    void testGetMemberById() {
        int id = 1;
        Member member = new Member(1, "John", "john@example.com", "1234567890");

        when(memberRepository.findById(id)).thenReturn(Optional.of(member));

        Optional<Member> result = memberService.getMemberById(id);

        assertTrue(result.isPresent());
        assertEquals("John", result.get().getName());
        verify(memberRepository, times(1)).findById(id);
    }

    @Test
    void testCreateMember() {
        Member member = new Member(null, "John", "john@example.com", "1234567890");

        when(sequenceGenerator.getNextSequence("member")).thenReturn(1);
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        Member result = memberService.createMember(member);

        assertNotNull(result);
        assertEquals("John", result.getName());
        verify(sequenceGenerator, times(1)).getNextSequence("member");
        verify(memberRepository, times(1)).save(member);
    }

    @Test
    void testUpdateMember() {
        int id = 1;
        Member existingMember = new Member(id, "John", "john@example.com", "1234567890");
        Member updatedDetails = new Member(null, "Jane", "jane@example.com", "0987654321");

        when(memberRepository.findById(id)).thenReturn(Optional.of(existingMember));
        when(memberRepository.save(existingMember)).thenReturn(existingMember);

        Member result = memberService.updateMember(id, updatedDetails);

        assertEquals("Jane", result.getName());
        assertEquals("jane@example.com", result.getEmail());
        assertEquals("0987654321", result.getPhone());
        verify(memberRepository, times(1)).findById(id);
        verify(memberRepository, times(1)).save(existingMember);
    }

    @Test
    void testDeleteMember() {
        int id = 1;

        when(memberRepository.existsById(id)).thenReturn(true);

        memberService.deleteMember(id);

        verify(memberRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteMemberThrowsException() {
        int id = 1;

        when(memberRepository.existsById(id)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> memberService.deleteMember(id));
        assertEquals("Member not found with id: " + id, exception.getMessage());
        verify(memberRepository, times(0)).deleteById(id);
    }
}
