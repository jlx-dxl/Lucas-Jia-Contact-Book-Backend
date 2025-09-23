package com.lucasjia.contactbookbackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/api/test/frontend")
    public String testConnection() {
        return "Backend is running!";
    }
}
