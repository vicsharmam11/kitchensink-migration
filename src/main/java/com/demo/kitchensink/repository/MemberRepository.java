package com.demo.kitchensink.repository;

import com.demo.kitchensink.model.Member;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends MongoRepository<Member, String> {
    // Custom query methods (if needed) can be added here

    // Find all members
    List<Member> findAll();

    // Find members by name
    List<Member> findByName(String name);

    // Find members by email
    Optional<Member> findByEmail(String email);

    // Find members whose name contains a substring (case-insensitive)
    List<Member> findByNameContainingIgnoreCase(String name);

    // Find members with phone starting with specific digits
    List<Member> findByPhoneStartingWith(String phonePrefix);

    // Check if a member exists by email
    boolean existsByEmail(String email);
}

