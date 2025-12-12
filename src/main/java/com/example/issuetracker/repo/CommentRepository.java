package com.example.issuetracker.repo;

import com.example.issuetracker.domain.CommentEntity;
import com.example.issuetracker.domain.IssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByIssue(IssueEntity issue);
}
