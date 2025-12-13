package com.example.tasktracker.dto.task;

import com.example.tasktracker.domain.TaskPriority;
import com.example.tasktracker.domain.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TaskResponse(
        Long id,
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        LocalDate dueDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long version,
        AssigneeShortResponse assignee
) {}
