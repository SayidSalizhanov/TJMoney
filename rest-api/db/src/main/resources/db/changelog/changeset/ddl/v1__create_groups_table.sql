CREATE TABLE groups
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR UNIQUE,
    created_at  TIMESTAMP,
    description VARCHAR
);