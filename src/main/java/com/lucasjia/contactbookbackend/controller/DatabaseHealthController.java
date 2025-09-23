package com.lucasjia.contactbookbackend.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DatabaseHealthController {
    private final JdbcTemplate jdbc;

    public DatabaseHealthController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @GetMapping("/api/health/db")
    public String dbHealth() {
        Integer one = jdbc.queryForObject("SELECT 1", Integer.class);
        return (one != null && one == 1) ? "DB OK" : "DB FAIL";
    }
}