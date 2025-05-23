CREATE TABLE applications
(
    id       BIGSERIAL PRIMARY KEY,
    user_id  BIGINT,
    group_id BIGINT,
    send_at  TIMESTAMP,
    status   VARCHAR,
    CONSTRAINT fk_applications_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_applications_group FOREIGN KEY (group_id) REFERENCES groups (id) ON DELETE CASCADE
);