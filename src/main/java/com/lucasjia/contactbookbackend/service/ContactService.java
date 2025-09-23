package com.lucasjia.contactbookbackend.service;

import com.lucasjia.contactbookbackend.dto.ContactRequest;
import com.lucasjia.contactbookbackend.dto.ContactResponse;
import com.lucasjia.contactbookbackend.entity.Contact;
import com.lucasjia.contactbookbackend.entity.User;
import com.lucasjia.contactbookbackend.repository.ContactRepository;
import com.lucasjia.contactbookbackend.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactService {
    private final ContactRepository contactRepository;
    private final UserRepository userRepository;

    public ContactService(ContactRepository contactRepository, UserRepository userRepository) {
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
    }

    // 🔹 创建联系人
    public ContactResponse createContact(Long userId, ContactRequest request) {
        // 检查用户是否存在
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 检查 email 是否重复
        if (contactRepository.existsByUserIdAndEmail(userId, request.getEmail())) {
            throw new IllegalArgumentException("This email is already in use");
        }

        // 检查 phone 是否重复
        if (contactRepository.existsByUserIdAndPhone(userId, request.getPhone())) {
            throw new IllegalArgumentException("This phone number is already in use");
        }

        // 保存联系人
        Contact contact = new Contact();
        contact.setUser(user);
        contact.setName(request.getName());
        contact.setEmail(request.getEmail().toLowerCase());
        contact.setPhone(request.getPhone());

        Contact saved = contactRepository.save(contact);

        return new ContactResponse(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.getPhone(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }

    // 🔹 获取联系人列表（可搜索）
    public List<ContactResponse> getContacts(Long userId, String q) {
        // 确认用户存在
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<Contact> contacts;

        if (q == null || q.isBlank()) {
            contacts = contactRepository.findByUserId(userId);
        } else {
            contacts = contactRepository.searchContacts(userId, q);
        }


        // 转换成 Response DTO
        return contacts.stream()
                .map(c -> new ContactResponse(
                        c.getId(),
                        c.getName(),
                        c.getEmail(),
                        c.getPhone(),
                        c.getCreatedAt(),
                        c.getUpdatedAt()
                ))
                .collect(Collectors.toList());
    }

}
