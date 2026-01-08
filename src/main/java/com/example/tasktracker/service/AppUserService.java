package com.example.tasktracker.service;

import com.example.tasktracker.domain.AppUser;
import com.example.tasktracker.dto.user.CreateUserRequest;
import com.example.tasktracker.dto.user.UserResponse;
import com.example.tasktracker.exception.ConflictException;
import com.example.tasktracker.exception.NotFoundException;
import com.example.tasktracker.repository.AppUserRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
//@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository repository;
    private final Set<String> sortFields = Set.of("fullName", "email", "id", "createdAt");

    private final Timer getUsersTimer;
    private final Timer getUserTimerFound;
    private final Timer getUserTimerNotFound;

    private final Counter userLookupFound;
    private final Counter userLookupNotFound;

    public AppUserService(AppUserRepository repository, MeterRegistry registry) {
        this.repository = repository;

        this.getUsersTimer = Timer.builder("app_get_users_seconds")
                .description("Time spent handling GET /api/users (service layer)")
                .publishPercentiles(0.5, 0.65, 0.95)
                .maximumExpectedValue(Duration.ofSeconds(5))
//                .publishPercentileHistogram()
                .register(registry);

        this.getUserTimerFound = Timer.builder("app_get_user_seconds")
                .description("Time spent handling GET /api/users/{id}")
                .tag("result", "found")
                .publishPercentileHistogram()
                .register(registry);

        this.getUserTimerNotFound = Timer.builder("app_get_user_seconds")
                .tag("result", "not_found")
                .publishPercentileHistogram()
                .register(registry);

        this.userLookupFound = Counter.builder("app_user_lookup_total")
                .description("User lookup outcomes")
                .tag("result", "found")
                .register(registry);

        this.userLookupNotFound = Counter.builder("app_user_lookup_total")
                .tag("result", "not_found")
                .register(registry);
    }

    @Transactional
    public UserResponse create(CreateUserRequest req) {
        repository.findByEmail(req.email())
                .ifPresent(u -> { throw new ConflictException("Email already exists: " + req.email()); });

        AppUser u = new AppUser();
        u.setEmail(req.email());
        u.setFullName(req.fullName());

        AppUser saved = repository.save(u);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public UserResponse get(long userId) {
        long startNanos = System.nanoTime();
        log.debug("get data about userId {}, startNanos = {}", userId, startNanos);
        Optional<AppUser> user = repository.findById(userId);
        long took = System.nanoTime() - startNanos;

        if (user.isPresent()) {
            userLookupFound.increment();
            getUserTimerFound.record(took, TimeUnit.NANOSECONDS);
            log.debug("found data about userId {},  tookNanos={}", userId, took);
        } else {
            userLookupNotFound.increment();
            getUserTimerNotFound.record(took, TimeUnit.NANOSECONDS);

            log.error("User with id {} not found", userId);
            throw new NotFoundException("User not found: " + userId);
        }

        return toResponse(user.get());
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> list(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
//        return repository.findAll(pageable).map(this::toListResponse);
        return getUsersTimer.record(() -> repository.findAll(pageable).map(this::toListResponse));
    }

    private UserResponse toResponse(AppUser u) {
        return new UserResponse(u.getId(), u.getEmail(), u.getFullName(), u.getCreatedAt());
    }
    private UserResponse toListResponse(AppUser u) {
        return new UserResponse(u.getId(), u.getEmail(), u.getFullName(), null);
    }
}
