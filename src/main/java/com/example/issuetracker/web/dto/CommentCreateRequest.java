package com.example.issuetracker.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentCreateRequest(
        @NotNull Long authorId,
        @NotBlank String body
) {
}
