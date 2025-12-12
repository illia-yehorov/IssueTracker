package com.example.issuetracker.web.dto;

import com.example.issuetracker.domain.IssuePriority;
import com.example.issuetracker.domain.IssueStatus;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record IssueUpdateRequest(
        @NotNull Long version,
        IssueStatus status,
        Long assigneeId,
        String title,
        String description,
        IssuePriority priority,
        Set<String> labels
) {
}
