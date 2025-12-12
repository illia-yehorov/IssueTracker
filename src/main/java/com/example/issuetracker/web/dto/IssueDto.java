package com.example.issuetracker.web.dto;

import com.example.issuetracker.domain.IssuePriority;
import com.example.issuetracker.domain.IssueStatus;

import java.time.LocalDateTime;
import java.util.Set;

public record IssueDto(Long id,
                       Long projectId,
                       Long reporterId,
                       Long assigneeId,
                       String title,
                       String description,
                       IssueStatus status,
                       IssuePriority priority,
                       Long version,
                       Set<String> labels,
                       LocalDateTime createdAt,
                       LocalDateTime updatedAt) {
}
