package com.example.issuetracker.repo;

import com.example.issuetracker.domain.LabelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LabelRepository extends JpaRepository<LabelEntity, Long> {
    Optional<LabelEntity> findByName(String name);
}
