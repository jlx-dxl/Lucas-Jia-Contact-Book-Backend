package com.lucasjia.contactbookbackend.controller;

import com.lucasjia.contactbookbackend.entity.Contact;
import com.lucasjia.contactbookbackend.repository.ContactRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContactDebugController {
    private final ContactRepository repo;
    public ContactDebugController(ContactRepository repo) { this.repo = repo; }

    @PostMapping("/api/debug/seed-one")
    public String seedOne() {
        Contact c = new Contact();
        c.setName("Alice");
        c.setEmail("alice@example.com");
        c.setPhone("1234567890");
        repo.save(c);
        return "INSERTED ID=" + c.getId();
    }
}
