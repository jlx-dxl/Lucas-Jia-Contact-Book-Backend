package com.lucasjia.contactbookbackend.controller;

import com.lucasjia.contactbookbackend.dto.ContactRequest;
import com.lucasjia.contactbookbackend.dto.ContactResponse;
import com.lucasjia.contactbookbackend.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {
    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    // 🔹 创建联系人
    @PostMapping
    public ResponseEntity<ContactResponse> createContact(
            @RequestParam Long userId, // 从请求参数里传用户 ID（简单版）
            @Valid @RequestBody ContactRequest request) {

        ContactResponse response = contactService.createContact(userId, request);
        return ResponseEntity.ok(response);
    }
}
