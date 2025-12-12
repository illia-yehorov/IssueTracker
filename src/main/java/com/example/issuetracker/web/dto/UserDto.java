package com.example.issuetracker.web.dto;

import java.time.LocalDateTime;

public record UserDto(Long id, String email, String fullName, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
