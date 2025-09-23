package com.lucasjia.contactbookbackend.repository;

import com.lucasjia.contactbookbackend.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;


public interface ContactRepository extends JpaRepository<Contact, Long> {
    boolean existsByUserIdAndEmail(Long userId, String email);
    boolean existsByUserIdAndPhone(Long userId, String phone);

    List<Contact> findByUserId(Long userId);

    List<Contact> findByUserIdAndNameContainingIgnoreCase(Long userId, String name);
    List<Contact> findByUserIdAndEmailContainingIgnoreCase(Long userId, String email);
    List<Contact> findByUserIdAndPhoneContaining(Long userId, String phone);

    @Query("SELECT c FROM Contact c WHERE c.user.id = :userId " +
            "AND (LOWER(c.name) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "OR LOWER(c.email) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "OR c.phone LIKE CONCAT('%', :q, '%'))")
    List<Contact> searchContacts(Long userId, String q);
}

