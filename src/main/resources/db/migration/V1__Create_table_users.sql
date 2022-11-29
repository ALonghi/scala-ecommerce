CREATE TABLE IF NOT EXISTS users
(
    id      bigint unique generated always as identity (minvalue 1 start with 1) PRIMARY KEY,
    email   text NOT NULL,
    name    text NOT NULL,
    surname text NOT NULL
);
