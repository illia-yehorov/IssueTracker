package com.example.issuetracker.web.dto;

import java.time.LocalDateTime;

public record ProjectDto(Long id, String projectKey, String name, Long ownerId, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
