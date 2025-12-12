package com.example.issuetracker.web.dto;

import java.time.LocalDateTime;

public record CommentDto(Long id, Long issueId, Long authorId, String body, LocalDateTime createdAt) {
}
