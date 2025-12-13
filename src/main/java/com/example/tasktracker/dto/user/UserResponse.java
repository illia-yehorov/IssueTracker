package com.example.tasktracker.dto.user;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String email,
        String fullName,
        LocalDateTime createdAt
) {}
