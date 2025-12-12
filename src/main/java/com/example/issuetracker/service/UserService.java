package com.example.issuetracker.service;

import com.example.issuetracker.domain.UserEntity;
import com.example.issuetracker.repo.UserRepository;
import com.example.issuetracker.web.dto.UserCreateRequest;
import com.example.issuetracker.web.dto.UserDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserDto createUser(UserCreateRequest request) {
        UserEntity user = UserEntity.builder()
                .email(request.email())
                .fullName(request.fullName())
                .build();
        return toDto(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UserEntity getUserEntity(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));
    }

    @Transactional(readOnly = true)
    public UserDto getUser(Long id) {
        return toDto(getUserEntity(id));
    }

    private UserDto toDto(UserEntity entity) {
        return new UserDto(entity.getId(), entity.getEmail(), entity.getFullName(), entity.getCreatedAt(), entity.getUpdatedAt());
    }
}
