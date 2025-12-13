package com.example.tasktracker.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
        @NotBlank @Email String email,
        @NotBlank String fullName
) {}
