create table users
(
    id uuid primary key,
    username varchar not null unique,
    password varchar not null
);