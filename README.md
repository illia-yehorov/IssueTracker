# tasktracker-jpa

Mini project to practice Spring Data JPA / Hibernate with PostgreSQL + Flyway.

## Requirements
- Java 17
- Docker (for PostgreSQL)
- Gradle installed (or generate wrapper once: `gradle wrapper`)

## Start PostgreSQL
```bash
docker compose up -d
```

Postgres will be available on `localhost:5432` with:
- db: `tasktracker`
- user: `tasktracker`
- password: `tasktracker`

## Run the app
From the project root:

If you have Gradle installed:
```bash
gradle bootRun
```

Or generate wrapper once and use it next time:
```bash
gradle wrapper
./gradlew bootRun
```

## What happens on startup
- Flyway runs migrations from `src/main/resources/db/migration`
- Hibernate validates schema (`spring.jpa.hibernate.ddl-auto=validate`)

## API

### Create user
```bash
curl -i -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"email":"john@example.com","fullName":"John Doe"}'
```

### Get user by id
```bash
curl -i http://localhost:8080/api/users/1
```

### Create task (assigneeId is optional)
```bash
curl -i -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title":"Learn Spring JPA",
    "description":"Start with @ManyToOne and @Version",
    "priority":"HIGH",
    "dueDate":"2026-01-10",
    "assigneeId": 1
  }'
```

Without assignee:
```bash
curl -i -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title":"Unassigned task",
    "priority":"LOW"
  }'
```

### Get task by id
```bash
curl -i http://localhost:8080/api/tasks/1
```

## Notes for Hibernate practice (next steps)
- Add list endpoints with pagination + filtering
- Add Task comments (OneToMany + orphanRemoval)
- Add tags (ManyToMany)
- Practice fetch strategies (N+1), EntityGraph, fetch join
- Add optimistic locking conflicts with @Version
