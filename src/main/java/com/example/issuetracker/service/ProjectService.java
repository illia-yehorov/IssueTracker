package com.example.issuetracker.service;

import com.example.issuetracker.domain.ProjectEntity;
import com.example.issuetracker.domain.UserEntity;
import com.example.issuetracker.repo.ProjectRepository;
import com.example.issuetracker.web.dto.ProjectCreateRequest;
import com.example.issuetracker.web.dto.ProjectDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserService userService;

    public ProjectService(ProjectRepository projectRepository, UserService userService) {
        this.projectRepository = projectRepository;
        this.userService = userService;
    }

    @Transactional
    public ProjectDto create(ProjectCreateRequest request) {
        UserEntity owner = userService.getUserEntity(request.ownerId());
        ProjectEntity project = ProjectEntity.builder()
                .projectKey(request.projectKey())
                .name(request.name())
                .owner(owner)
                .build();
        return toDto(projectRepository.save(project));
    }

    @Transactional(readOnly = true)
    public ProjectEntity getProjectEntity(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id " + id));
    }

    @Transactional(readOnly = true)
    public ProjectDto get(Long id) {
        return toDto(getProjectEntity(id));
    }

    @Transactional(readOnly = true)
    public List<ProjectDto> list() {
        return projectRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private ProjectDto toDto(ProjectEntity entity) {
        return new ProjectDto(entity.getId(), entity.getProjectKey(), entity.getName(), entity.getOwner().getId(),
                entity.getCreatedAt(), entity.getUpdatedAt());
    }
}
