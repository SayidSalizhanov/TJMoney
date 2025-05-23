CREATE TABLE avatars
(
    id      BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    url     VARCHAR,
    CONSTRAINT fk_avatars_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);