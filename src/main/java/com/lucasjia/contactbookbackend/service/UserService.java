package com.lucasjia.contactbookbackend.service;

import com.lucasjia.contactbookbackend.dto.UserRequest;
import com.lucasjia.contactbookbackend.dto.UserResponse;
import com.lucasjia.contactbookbackend.entity.User;
import com.lucasjia.contactbookbackend.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder(); // 密码加密器
    }

    public UserResponse register(UserRequest request) {
        // 检查 email 是否已存在
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }

        // 创建用户并保存
        User user = new User();
        user.setEmail(request.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // 加密存储

        User saved = userRepository.save(user);

        return new UserResponse(saved.getId(), saved.getEmail(), saved.getCreatedAt());
    }

    public UserResponse login(UserRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        return new UserResponse(user.getId(), user.getEmail(), user.getCreatedAt());
    }
}
