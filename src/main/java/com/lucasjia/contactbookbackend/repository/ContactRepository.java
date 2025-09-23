package com.lucasjia.contactbookbackend.repository;

import com.lucasjia.contactbookbackend.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    boolean existsByUserIdAndEmail(Long userId, String email);
    boolean existsByUserIdAndPhone(Long userId, String phone);

    List<Contact> findByUserId(Long userId);

    List<Contact> findByUserIdAndNameContainingIgnoreCase(Long userId, String name);
    List<Contact> findByUserIdAndEmailContainingIgnoreCase(Long userId, String email);
    List<Contact> findByUserIdAndPhoneContaining(Long userId, String phone);
}

