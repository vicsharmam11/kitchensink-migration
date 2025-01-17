package com.demo.kitchensink.service;

import com.demo.kitchensink.exception.CustomException;
import com.demo.kitchensink.model.Member;
import com.demo.kitchensink.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public List<Member> getUsersByName(String name) {
        return memberRepository.findByName(name);
    }

    public List<Member> searchUsersByName(String name) {
        return memberRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Member> getUsersByPhonePrefix(String prefix) {
        return memberRepository.findByPhoneStartingWith(prefix);
    }


    public boolean doesUserExistByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }


    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }


    public Optional<Member> getMemberById(String id) {
        return memberRepository.findById(id);
    }

    public Member createMember(Member member) {
        return memberRepository.save(member);
    }

    public Member updateMember(String id, Member memberDetails) {
        return memberRepository.findById(id).map(member -> {
            if (memberDetails.getName() != null && !memberDetails.getName().isBlank() ) {
                member.setName(memberDetails.getName());
            }
            if (memberDetails.getEmail() != null && !memberDetails.getEmail().isBlank()) {
                member.setEmail(memberDetails.getEmail());
            }
            if (memberDetails.getPhone() != null && !memberDetails.getPhone().isBlank()) {
                member.setPhone(memberDetails.getPhone());
            }
            return memberRepository.save(member);
        }).orElseThrow(() -> new RuntimeException("Member not found with id: " + id));
    }


    public void deleteMember(String id) {
        if (!memberRepository.existsById(id)) {
            throw new RuntimeException("Member not found with id: " + id);
        }
        memberRepository.deleteById(id);
    }










}

