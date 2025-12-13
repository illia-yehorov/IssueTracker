-- V2: seed data (optional)

insert into app_user(email, full_name)
values
    ('alice@example.com', 'Alice Cooper'),
    ('bob@example.com', 'Bob Marley'),
    ('carol@example.com', 'Carol King'),
    ('dave@example.com', 'Dave Grohl'),
    ('eve@example.com', 'Eve Adams'),
    ('frank@example.com', 'Frank Sinatra'),
    ('grace@example.com', 'Grace Hopper'),
    ('heidi@example.com', 'Heidi Klum'),
    ('ivan@example.com', 'Ivan Petrov'),
    ('judy@example.com', 'Judy Garland'),
    ('mallory@example.com', 'Mallory Knox'),
    ('oscar@example.com', 'Oscar Wilde'),
    ('peggy@example.com', 'Peggy Carter'),
    ('trent@example.com', 'Trent Reznor'),
    ('victor@example.com', 'Victor Hugo'),
    ('walter@example.com', 'Walter White'),
    ('yvonne@example.com', 'Yvonne Strahovski'),
    ('zara@example.com', 'Zara Larsson'),
    ('mia@example.com', 'Mia Wallace'),
    ('leo@example.com', 'Leo Tolstoy')
on conflict (email) do nothing;


-- Create tasks (assignee optional)
insert into task(title, description, status, priority, due_date, assignee_id)
values
('Buy groceries', 'Milk, eggs, bread', 'NEW', 'MEDIUM', current_date + 3, (select id from app_user where email = 'alice@example.com')),
('Read Hibernate docs', 'Focus on @ManyToOne, @Version, fetching', 'IN_PROGRESS', 'HIGH', current_date + 10, null);
