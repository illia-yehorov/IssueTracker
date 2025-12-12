package com.example.issuetracker.service;

import com.example.issuetracker.domain.*;
import com.example.issuetracker.repo.IssueRepository;
import com.example.issuetracker.repo.LabelRepository;
import com.example.issuetracker.web.dto.IssueCreateRequest;
import com.example.issuetracker.web.dto.IssueDto;
import com.example.issuetracker.web.dto.IssueUpdateRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class IssueService {

    private final IssueRepository issueRepository;
    private final ProjectService projectService;
    private final UserService userService;
    private final LabelRepository labelRepository;

    public IssueService(IssueRepository issueRepository, ProjectService projectService, UserService userService, LabelRepository labelRepository) {
        this.issueRepository = issueRepository;
        this.projectService = projectService;
        this.userService = userService;
        this.labelRepository = labelRepository;
    }

    @Transactional
    public IssueDto create(Long projectId, IssueCreateRequest request) {
        ProjectEntity project = projectService.getProjectEntity(projectId);
        UserEntity reporter = userService.getUserEntity(request.reporterId());
        UserEntity assignee = request.assigneeId() != null ? userService.getUserEntity(request.assigneeId()) : null;
        Set<LabelEntity> labels = upsertLabels(request.labels());

        IssueEntity issue = IssueEntity.builder()
                .project(project)
                .reporter(reporter)
                .assignee(assignee)
                .title(request.title())
                .description(request.description())
                .priority(request.priority())
                .status(IssueStatus.OPEN)
                .labels(labels)
                .build();
        return toDto(issueRepository.save(issue));
    }

    @Transactional(readOnly = true)
    public IssueEntity getIssueEntity(Long id) {
        return issueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Issue not found with id " + id));
    }

    @Transactional(readOnly = true)
    public IssueDto get(Long id) {
        return toDto(getIssueEntity(id));
    }

    @Transactional(readOnly = true)
    public Page<IssueDto> findByProject(Long projectId, IssueStatus status, Long assigneeId, String labelName, Pageable pageable) {
        Specification<IssueEntity> spec = where(projectEquals(projectId))
                .and(statusEquals(status))
                .and(assigneeEquals(assigneeId))
                .and(labelEquals(labelName));
        return issueRepository.findAll(spec, pageable).map(this::toDto);
    }

    @Transactional
    public IssueDto update(Long id, IssueUpdateRequest request) {
        IssueEntity issue = getIssueEntity(id);
        if (!issue.getVersion().equals(request.version())) {
            throw new ObjectOptimisticLockingFailureException(IssueEntity.class, id);
        }
        if (request.status() != null) {
            issue.setStatus(request.status());
        }
        if (request.assigneeId() != null) {
            issue.setAssignee(userService.getUserEntity(request.assigneeId()));
        }
        if (request.title() != null) {
            issue.setTitle(request.title());
        }
        if (request.description() != null) {
            issue.setDescription(request.description());
        }
        if (request.priority() != null) {
            issue.setPriority(request.priority());
        }
        if (request.labels() != null) {
            issue.setLabels(upsertLabels(request.labels()));
        }
        return toDto(issueRepository.save(issue));
    }

    private Set<LabelEntity> upsertLabels(Set<String> labelNames) {
        if (labelNames == null || labelNames.isEmpty()) {
            return new HashSet<>();
        }
        return labelNames.stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(name -> labelRepository.findByName(name)
                        .orElseGet(() -> labelRepository.save(LabelEntity.builder().name(name).build())))
                .collect(Collectors.toSet());
    }

    private IssueDto toDto(IssueEntity entity) {
        Set<String> labels = entity.getLabels().stream().map(LabelEntity::getName).collect(Collectors.toSet());
        return new IssueDto(entity.getId(),
                entity.getProject().getId(),
                entity.getReporter().getId(),
                entity.getAssignee() != null ? entity.getAssignee().getId() : null,
                entity.getTitle(),
                entity.getDescription(),
                entity.getStatus(),
                entity.getPriority(),
                entity.getVersion(),
                labels,
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    private Specification<IssueEntity> projectEquals(Long projectId) {
        return (root, query, cb) -> cb.equal(root.get("project").get("id"), projectId);
    }

    private Specification<IssueEntity> statusEquals(IssueStatus status) {
        return status == null ? null : (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    private Specification<IssueEntity> assigneeEquals(Long assigneeId) {
        return assigneeId == null ? null : (root, query, cb) -> cb.equal(root.get("assignee").get("id"), assigneeId);
    }

    private Specification<IssueEntity> labelEquals(String labelName) {
        if (labelName == null || labelName.isBlank()) {
            return null;
        }
        return (root, query, cb) -> {
            query.distinct(true);
            return cb.equal(root.join("labels").get("name"), labelName);
        };
    }
}
