package com.example.issuetracker.repo;

import com.example.issuetracker.domain.IssueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IssueRepository extends JpaRepository<IssueEntity, Long>, JpaSpecificationExecutor<IssueEntity> {
}
