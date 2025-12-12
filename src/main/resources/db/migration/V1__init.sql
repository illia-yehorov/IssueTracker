CREATE TABLE users (
    id          BIGSERIAL PRIMARY KEY,
    email       VARCHAR(255) NOT NULL UNIQUE,
    full_name   VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT now(),
    updated_at  TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE projects (
    id           BIGSERIAL PRIMARY KEY,
    project_key  VARCHAR(20) NOT NULL UNIQUE,
    name         VARCHAR(255) NOT NULL,
    owner_id     BIGINT NOT NULL REFERENCES users(id),
    created_at   TIMESTAMP NOT NULL DEFAULT now(),
    updated_at   TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE issues (
    id           BIGSERIAL PRIMARY KEY,
    project_id   BIGINT NOT NULL REFERENCES projects(id),
    reporter_id  BIGINT NOT NULL REFERENCES users(id),
    assignee_id  BIGINT REFERENCES users(id),
    title        VARCHAR(255) NOT NULL,
    description  TEXT,
    status       VARCHAR(30) NOT NULL,
    priority     VARCHAR(30) NOT NULL,
    version      BIGINT NOT NULL DEFAULT 0,
    created_at   TIMESTAMP NOT NULL DEFAULT now(),
    updated_at   TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_issues_project ON issues(project_id);
CREATE INDEX idx_issues_status ON issues(status);
CREATE INDEX idx_issues_assignee ON issues(assignee_id);

CREATE TABLE comments (
    id          BIGSERIAL PRIMARY KEY,
    issue_id    BIGINT NOT NULL REFERENCES issues(id) ON DELETE CASCADE,
    author_id   BIGINT NOT NULL REFERENCES users(id),
    body        TEXT NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_comments_issue ON comments(issue_id);

CREATE TABLE labels (
    id     BIGSERIAL PRIMARY KEY,
    name   VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE issue_labels (
    issue_id BIGINT NOT NULL REFERENCES issues(id) ON DELETE CASCADE,
    label_id BIGINT NOT NULL REFERENCES labels(id) ON DELETE CASCADE,
    PRIMARY KEY (issue_id, label_id)
);
