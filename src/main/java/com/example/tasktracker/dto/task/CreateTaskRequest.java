package com.example.tasktracker.dto.task;

import com.example.tasktracker.domain.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateTaskRequest(
        @NotBlank String title,
        String description,
        @NotNull TaskPriority priority,
        LocalDate dueDate,
        Long assigneeId
) {}
