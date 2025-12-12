package com.example.issuetracker.web;

import com.example.issuetracker.service.ProjectService;
import com.example.issuetracker.web.dto.ProjectCreateRequest;
import com.example.issuetracker.web.dto.ProjectDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectDto create(@Valid @RequestBody ProjectCreateRequest request) {
        return projectService.create(request);
    }

    @GetMapping("/{id}")
    public ProjectDto get(@PathVariable Long id) {
        return projectService.get(id);
    }

    @GetMapping
    public List<ProjectDto> list() {
        return projectService.list();
    }
}
