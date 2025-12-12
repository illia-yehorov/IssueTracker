INSERT INTO users (email, full_name) VALUES
('alice@example.com', 'Alice Doe'),
('bob@example.com', 'Bob Roe'),
('charlie@example.com', 'Charlie Smith');

INSERT INTO projects (project_key, name, owner_id)
VALUES ('APP', 'App Platform', 1),
       ('PAY', 'Payments', 2);

INSERT INTO labels (name) VALUES ('backend'), ('frontend'), ('urgent');

INSERT INTO issues (project_id, reporter_id, assignee_id, title, description, status, priority)
VALUES (1, 1, 2, 'Landing page is slow', 'Investigate performance', 'OPEN', 'HIGH'),
       (2, 2, 3, 'Payments failing', 'Checkout errors on VISA', 'IN_PROGRESS', 'HIGH'),
       (1, 3, NULL, 'Add feature flag', 'Feature toggle for beta', 'OPEN', 'MEDIUM');

INSERT INTO comments (issue_id, author_id, body)
VALUES (1, 1, 'Observed high TTFB'),
       (2, 2, 'Customer reported multiple times');

INSERT INTO issue_labels (issue_id, label_id)
VALUES (1, 1),
       (1, 3),
       (2, 1),
       (2, 3),
       (3, 2);
