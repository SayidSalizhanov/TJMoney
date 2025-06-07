CREATE TABLE articles
(
    id           BIGSERIAL PRIMARY KEY,
    title        VARCHAR,
    content      VARCHAR,
    author       VARCHAR,
    published_at TIMESTAMP
);