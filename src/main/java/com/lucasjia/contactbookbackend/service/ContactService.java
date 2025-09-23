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
        if (contactRepository.existsByUserIdAndPhone(userId, normalizePhone(request.getPhone()))) {
            throw new IllegalArgumentException("This phone number is already in use");
        }

        // 保存联系人
        Contact contact = new Contact();
        contact.setUser(user);
        contact.setName(request.getName());
        contact.setEmail(request.getEmail().toLowerCase());
        contact.setPhone(normalizePhone(request.getPhone())); // ✅ 格式化

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
            // ✅ 对搜索关键字做标准化
            String normalized = normalizePhone(q);

            // 如果 normalized 和原始输入不同，可以同时查
            contacts = contactRepository.searchContacts(userId, normalized);
            if (!normalized.equals(q)) {
                contacts.addAll(contactRepository.searchContacts(userId, q));
            }

            // 去重
            contacts = contacts.stream().distinct().collect(Collectors.toList());
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


    // 🔹 更新联系人
    public ContactResponse updateContact(Long userId, Long contactId, ContactRequest request) {
        // 确认用户存在
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 查找联系人
        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new IllegalArgumentException("Contact not found"));

        // 确保联系人属于该用户
        if (!contact.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Contact does not belong to this user");
        }

        // 检查 email 是否重复（排除自己）
        if (!contact.getEmail().equalsIgnoreCase(request.getEmail()) &&
                contactRepository.existsByUserIdAndEmail(userId, request.getEmail())) {
            throw new IllegalArgumentException("This email is already in use");
        }

        // 检查 phone 是否重复（排除自己）
        if (!contact.getPhone().equals(request.getPhone()) &&
                contactRepository.existsByUserIdAndPhone(userId, normalizePhone(request.getPhone()))) {
            throw new IllegalArgumentException("This phone number is already in use");
        }

        // 更新信息
        contact.setName(request.getName());
        contact.setEmail(request.getEmail().toLowerCase());
        contact.setPhone(normalizePhone(request.getPhone())); // ✅ 格式化

        Contact updated = contactRepository.save(contact);

        return new ContactResponse(
                updated.getId(),
                updated.getName(),
                updated.getEmail(),
                updated.getPhone(),
                updated.getCreatedAt(),
                updated.getUpdatedAt()
        );
    }

    // 🔹 删除联系人
    public void deleteContact(Long userId, Long contactId) {
        // 确认用户存在
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 查找联系人
        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new IllegalArgumentException("Contact not found"));

        // 确保联系人属于该用户
        if (!contact.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Contact does not belong to this user");
        }

        // 删除联系人
        contactRepository.delete(contact);
    }

    private String normalizePhone(String phone) {
        if (phone == null) return null;

        // 去掉所有空格、连字符、括号
        String normalized = phone.replaceAll("[\\s\\-()]", "");

        // 如果没有国家码，默认加上 +1（你可以改成项目需要的默认区号）
        if (!normalized.startsWith("+")) {
            normalized = "+1" + normalized;
        }

        return normalized;
    }
}
