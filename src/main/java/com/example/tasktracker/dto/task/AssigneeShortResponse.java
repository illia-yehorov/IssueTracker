package com.example.tasktracker.dto.task;

public record AssigneeShortResponse(
        Long id,
        String email,
        String fullName
) {}
