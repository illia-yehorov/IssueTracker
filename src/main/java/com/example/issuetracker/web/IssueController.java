package com.example.issuetracker.web;

import com.example.issuetracker.domain.IssueStatus;
import com.example.issuetracker.service.IssueService;
import com.example.issuetracker.web.dto.IssueCreateRequest;
import com.example.issuetracker.web.dto.IssueDto;
import com.example.issuetracker.web.dto.IssueUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class IssueController {

    private final IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    @PostMapping("/projects/{projectId}/issues")
    @ResponseStatus(HttpStatus.CREATED)
    public IssueDto createIssue(@PathVariable Long projectId, @Valid @RequestBody IssueCreateRequest request) {
        return issueService.create(projectId, request);
    }

    @GetMapping("/issues/{id}")
    public IssueDto getIssue(@PathVariable Long id) {
        return issueService.get(id);
    }

    @GetMapping("/projects/{projectId}/issues")
    public Page<IssueDto> listIssues(@PathVariable Long projectId,
                                     @RequestParam(required = false) IssueStatus status,
                                     @RequestParam(required = false) Long assigneeId,
                                     @RequestParam(required = false) String label,
                                     Pageable pageable) {
        return issueService.findByProject(projectId, status, assigneeId, label, pageable);
    }

    @PatchMapping("/issues/{id}")
    public IssueDto updateIssue(@PathVariable Long id, @Valid @RequestBody IssueUpdateRequest request) {
        return issueService.update(id, request);
    }
}
