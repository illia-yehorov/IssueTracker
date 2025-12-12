package com.example.issuetracker.web.dto;

import com.example.issuetracker.domain.IssuePriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record IssueCreateRequest(
        @NotNull Long reporterId,
        Long assigneeId,
        @NotBlank String title,
        String description,
        @NotNull IssuePriority priority,
        Set<String> labels
) {
}
