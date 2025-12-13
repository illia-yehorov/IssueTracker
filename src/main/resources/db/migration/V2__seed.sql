-- V2: seed data (optional)

insert into app_user(email, full_name)
values
('alice@example.com', 'Alice Cooper'),
('bob@example.com', 'Bob Marley')
on conflict (email) do nothing;

-- Create tasks (assignee optional)
insert into task(title, description, status, priority, due_date, assignee_id)
values
('Buy groceries', 'Milk, eggs, bread', 'NEW', 'MEDIUM', current_date + 3, (select id from app_user where email = 'alice@example.com')),
('Read Hibernate docs', 'Focus on @ManyToOne, @Version, fetching', 'IN_PROGRESS', 'HIGH', current_date + 10, null);
