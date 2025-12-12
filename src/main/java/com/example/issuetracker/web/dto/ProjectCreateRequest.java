package com.example.issuetracker.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProjectCreateRequest(
        @NotBlank @Size(max = 20) String projectKey,
        @NotBlank String name,
        @NotNull Long ownerId
) {
}
