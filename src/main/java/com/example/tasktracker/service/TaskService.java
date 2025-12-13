package com.example.tasktracker.service;

import com.example.tasktracker.domain.AppUser;
import com.example.tasktracker.domain.Task;
import com.example.tasktracker.domain.TaskStatus;
import com.example.tasktracker.dto.task.AssigneeShortResponse;
import com.example.tasktracker.dto.task.CreateTaskRequest;
import com.example.tasktracker.dto.task.TaskResponse;
import com.example.tasktracker.exception.NotFoundException;
import com.example.tasktracker.repository.AppUserRepository;
import com.example.tasktracker.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final AppUserRepository userRepository;

    @Transactional
    public TaskResponse create(CreateTaskRequest req) {
        Task task = new Task();
        task.setTitle(req.title());
        task.setDescription(req.description());
        task.setPriority(req.priority());
        task.setDueDate(req.dueDate());
        task.setStatus(TaskStatus.NEW);

        if (req.assigneeId() != null) {
            AppUser assignee = userRepository.findById(req.assigneeId())
                    .orElseThrow(() -> new NotFoundException("Assignee not found: " + req.assigneeId()));
            task.setAssignee(assignee);
        }

        Task saved = taskRepository.save(task);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public TaskResponse get(long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task not found: " + id));
        return toResponse(task);
    }

    private TaskResponse toResponse(Task t) {
        AssigneeShortResponse assignee = null;
        if (t.getAssignee() != null) {
            // Accessing assignee fields is OK within transaction (read-only in get() / write in create()).
            var a = t.getAssignee();
            assignee = new AssigneeShortResponse(a.getId(), a.getEmail(), a.getFullName());
        }

        return new TaskResponse(
                t.getId(),
                t.getTitle(),
                t.getDescription(),
                t.getStatus(),
                t.getPriority(),
                t.getDueDate(),
                t.getCreatedAt(),
                t.getUpdatedAt(),
                t.getVersion(),
                assignee
        );
    }
}
