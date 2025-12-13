package com.example.tasktracker.service;

import com.example.tasktracker.domain.AppUser;
import com.example.tasktracker.dto.user.CreateUserRequest;
import com.example.tasktracker.dto.user.UserResponse;
import com.example.tasktracker.exception.ConflictException;
import com.example.tasktracker.exception.NotFoundException;
import com.example.tasktracker.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository repository;

    @Transactional
    public UserResponse create(CreateUserRequest req) {
        repository.findByEmail(req.email())
                .ifPresent(u -> { throw new ConflictException("Email already exists: " + req.email()); });

        AppUser u = new AppUser();
        u.setEmail(req.email());
        u.setFullName(req.fullName());

        AppUser saved = repository.save(u);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public UserResponse get(long id) {
        AppUser user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
        return toResponse(user);
    }

    private UserResponse toResponse(AppUser u) {
        return new UserResponse(u.getId(), u.getEmail(), u.getFullName(), u.getCreatedAt());
    }
}
