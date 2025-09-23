package com.lucasjia.contactbookbackend.repository;

import com.lucasjia.contactbookbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);   // check if the email already exist
    Optional<User> findByEmail(String email);   // sign in
}
