CREATE TABLE IF NOT EXISTS users(
    id serial primary key,
    name varchar(255),
    email varchar(255),
    password varchar(255)
);