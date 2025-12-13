package com.example.tasktracker.controller;

import com.example.tasktracker.dto.task.CreateTaskRequest;
import com.example.tasktracker.dto.task.TaskResponse;
import com.example.tasktracker.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TasksController {

    private final TaskService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse create(@Valid @RequestBody CreateTaskRequest req) {
        return service.create(req);
    }

    @GetMapping("/{id}")
    public TaskResponse get(@PathVariable long id) {
        return service.get(id);
    }
}
