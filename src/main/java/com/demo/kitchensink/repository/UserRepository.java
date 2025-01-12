package com.demo.kitchensink.repository;

import com.demo.kitchensink.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    // Query to find a user by username
    Optional<User> findByUsername(String username);
}

