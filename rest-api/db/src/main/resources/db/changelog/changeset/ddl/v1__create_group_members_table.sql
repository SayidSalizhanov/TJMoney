CREATE TABLE group_members
(
    id        BIGSERIAL PRIMARY KEY,
    user_id   BIGINT,
    group_id  BIGINT,
    joined_at TIMESTAMP,
    role      VARCHAR,
    CONSTRAINT fk_group_members_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_group_members_group FOREIGN KEY (group_id) REFERENCES groups (id) ON DELETE CASCADE
);