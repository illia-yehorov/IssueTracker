package com.example.issuetracker.web;

import com.example.issuetracker.service.CommentService;
import com.example.issuetracker.web.dto.CommentCreateRequest;
import com.example.issuetracker.web.dto.CommentDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues/{issueId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@PathVariable Long issueId, @Valid @RequestBody CommentCreateRequest request) {
        return commentService.addComment(issueId, request);
    }

    @GetMapping
    public List<CommentDto> list(@PathVariable Long issueId) {
        return commentService.listComments(issueId);
    }
}
