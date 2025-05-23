CREATE TABLE goals
(
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT,
    group_id    BIGINT,
    title       VARCHAR,
    description VARCHAR,
    progress    INTEGER,
    CONSTRAINT fk_goals_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_goals_group FOREIGN KEY (group_id) REFERENCES groups (id) ON DELETE CASCADE
);