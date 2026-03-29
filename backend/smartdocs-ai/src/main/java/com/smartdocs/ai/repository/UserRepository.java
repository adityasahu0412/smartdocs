package com.smartdocs.ai.repository;

import com.smartdocs.ai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// only need one custom method - find user by email during login

public interface UserRepository extends JpaRepository<User, Long> {

    // used in UserService to check if user exists with this email
    Optional<User> findByEmail(String email);
}