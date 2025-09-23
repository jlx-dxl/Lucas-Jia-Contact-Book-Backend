package com.lucasjia.contactbookbackend.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.lucasjia.contactbookbackend.dto.UserRequest;
import com.lucasjia.contactbookbackend.dto.UserResponse;
import com.lucasjia.contactbookbackend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest request) {
        UserResponse response = userService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody UserRequest request) {
        UserResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }
}
