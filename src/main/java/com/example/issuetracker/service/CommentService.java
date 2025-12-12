package com.example.issuetracker.service;

import com.example.issuetracker.domain.CommentEntity;
import com.example.issuetracker.domain.IssueEntity;
import com.example.issuetracker.domain.UserEntity;
import com.example.issuetracker.repo.CommentRepository;
import com.example.issuetracker.web.dto.CommentCreateRequest;
import com.example.issuetracker.web.dto.CommentDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final IssueService issueService;
    private final UserService userService;

    public CommentService(CommentRepository commentRepository, IssueService issueService, UserService userService) {
        this.commentRepository = commentRepository;
        this.issueService = issueService;
        this.userService = userService;
    }

    @Transactional
    public CommentDto addComment(Long issueId, CommentCreateRequest request) {
        IssueEntity issue = issueService.getIssueEntity(issueId);
        UserEntity author = userService.getUserEntity(request.authorId());
        CommentEntity comment = CommentEntity.builder()
                .issue(issue)
                .author(author)
                .body(request.body())
                .build();
        return toDto(commentRepository.save(comment));
    }

    @Transactional(readOnly = true)
    public List<CommentDto> listComments(Long issueId) {
        IssueEntity issue = issueService.getIssueEntity(issueId);
        return commentRepository.findByIssue(issue).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private CommentDto toDto(CommentEntity entity) {
        return new CommentDto(entity.getId(), entity.getIssue().getId(), entity.getAuthor().getId(), entity.getBody(), entity.getCreatedAt());
    }
}
