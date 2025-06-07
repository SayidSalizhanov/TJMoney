CREATE TABLE records
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT,
    group_id   BIGINT,
    title      VARCHAR,
    content    VARCHAR,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_records_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_records_group FOREIGN KEY (group_id) REFERENCES groups (id) ON DELETE CASCADE
);