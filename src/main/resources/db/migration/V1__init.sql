-- V1: initial schema for mini Task Tracker

create table if not exists app_user (
    id bigserial primary key,
    email varchar(255) not null unique,
    full_name varchar(255) not null,
    created_at timestamp not null default now()
);

create table if not exists task (
    id bigserial primary key,
    title varchar(255) not null,
    description text null,
    status varchar(32) not null,
    priority varchar(16) not null,
    due_date date null,
    assignee_id bigint null references app_user(id) on delete set null,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),
    version bigint not null default 0
);

create index if not exists idx_task_assignee_id on task(assignee_id);
create index if not exists idx_task_status on task(status);
