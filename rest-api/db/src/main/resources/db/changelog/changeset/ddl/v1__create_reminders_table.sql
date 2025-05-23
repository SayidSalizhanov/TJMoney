CREATE TABLE reminders
(
    id       BIGSERIAL PRIMARY KEY,
    user_id  BIGINT,
    group_id BIGINT,
    title    VARCHAR,
    message  VARCHAR,
    send_at  TIMESTAMP,
    status   VARCHAR,
    CONSTRAINT fk_reminders_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_reminders_group FOREIGN KEY (group_id) REFERENCES groups (id) ON DELETE CASCADE
);