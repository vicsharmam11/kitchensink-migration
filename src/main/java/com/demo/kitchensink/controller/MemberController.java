package com.demo.kitchensink.controller;

import ch.qos.logback.core.util.StringUtil;
import com.demo.kitchensink.exception.CustomException;
import com.demo.kitchensink.model.Member;
import com.demo.kitchensink.utils.JwtUtil;
import com.demo.kitchensink.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/members")
@Validated

public class MemberController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private JwtUtil jwtUtil;

    // Get all members
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Member>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }
    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptions() {
        return ResponseEntity.ok().build();
    }

    // Get a member by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Member> getMemberById(@PathVariable String id) {
        Optional<Member> member=memberService.getMemberById(id);
        if(!member.isPresent()){
            throw new CustomException("id", "Invalid Id provided");
        }
        return member.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create a new member
    @PostMapping
   @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Member> createMember(@Valid @RequestBody Member member) {

            if (StringUtil.isNullOrEmpty(member.getName())) {
                throw new CustomException("name","Name is mandatory");
            }
            if (StringUtil.isNullOrEmpty(member.getEmail())) {
                throw new CustomException("email","Email is mandatory");
            }
            if (StringUtil.isNullOrEmpty(member.getPhone())) {
                throw new CustomException("phone","Phone is mandatory");
            }
            Member savedMember = memberService.createMember(member);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedMember);
    }

    // Update a member by ID
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Member> updateMember(@PathVariable String id, @Valid @RequestBody Member memberDetails) {

            Optional<Member> member=memberService.getMemberById(id);
            if(!member.isPresent()){
                throw new CustomException("id","Invalid Id provided");
            }
            Member updatedMember = memberService.updateMember(id, memberDetails);
            if(updatedMember==null)
                return ResponseEntity.notFound().build();
            return ResponseEntity.ok(updatedMember);

    }

    // Delete a member by ID
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMember(@PathVariable String id) {
        try {
            memberService.deleteMember(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

