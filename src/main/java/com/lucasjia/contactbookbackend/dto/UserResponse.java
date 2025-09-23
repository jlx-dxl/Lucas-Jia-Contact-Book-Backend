package com.lucasjia.contactbookbackend.dto;

import java.time.Instant;

public class UserResponse {
    private Long id;
    private String email;
    private Instant createdAt;

    // --- Constructors ---
    public UserResponse(Long id, String email, Instant createdAt) {
        this.id = id;
        this.email = email;
        this.createdAt = createdAt;
    }

    // --- Getters ---
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public Instant getCreatedAt() { return createdAt; }
}
