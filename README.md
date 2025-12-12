# Issue Tracker

A minimal Issue Tracker REST API built with Spring Boot 3, Spring Data JPA, Flyway, and PostgreSQL.

## Requirements
- Java 17
- Maven
- Docker (for local PostgreSQL)

## Running locally
1. Start PostgreSQL using Docker Compose:
   ```bash
   docker-compose up -d
   ```
2. Run the application (Flyway migrations apply automatically):
   ```bash
   mvn spring-boot:run
   ```

Application runs on `http://localhost:8080` by default.

## API Overview

### Users
- **Create user**
  ```bash
  curl -X POST http://localhost:8080/api/users \
    -H "Content-Type: application/json" \
    -d '{"email":"new@example.com","fullName":"New User"}'
  ```
- **Get user**
  ```bash
  curl http://localhost:8080/api/users/1
  ```

### Projects
- **Create project**
  ```bash
  curl -X POST http://localhost:8080/api/projects \
    -H "Content-Type: application/json" \
    -d '{"projectKey":"APP","name":"App Platform","ownerId":1}'
  ```
- **List projects**
  ```bash
  curl http://localhost:8080/api/projects
  ```

### Issues
- **Create issue**
  ```bash
  curl -X POST http://localhost:8080/api/projects/1/issues \
    -H "Content-Type: application/json" \
    -d '{"reporterId":1,"assigneeId":2,"title":"Bug","description":"Fix it","priority":"HIGH","labels":["backend","urgent"]}'
  ```
- **List issues for project with paging and filters**
  ```bash
  curl "http://localhost:8080/api/projects/1/issues?status=OPEN&assigneeId=2&label=backend&page=0&size=10&sort=createdAt,desc"
  ```
- **Update issue (optimistic locking)**
  ```bash
  curl -X PATCH http://localhost:8080/api/issues/1 \
    -H "Content-Type: application/json" \
    -d '{"version":0,"status":"IN_PROGRESS","assigneeId":3,"labels":["backend"]}'
  ```

### Comments
- **Add comment**
  ```bash
  curl -X POST http://localhost:8080/api/issues/1/comments \
    -H "Content-Type: application/json" \
    -d '{"authorId":1,"body":"Looking into this."}'
  ```
- **List comments**
  ```bash
  curl http://localhost:8080/api/issues/1/comments
  ```

## Notes
- Hibernate DDL auto-generation is disabled; Flyway manages schema creation.
- Uses optimistic locking on issues via the `version` column.
