CREATE TABLE chat_messages
(
    id       BIGSERIAL PRIMARY KEY,
    content  TEXT         NOT NULL,
    type     VARCHAR(255) NOT NULL,
    datetime TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id  BIGINT       NOT NULL,
    group_id BIGINT       NOT NULL,

    CONSTRAINT fk_chat_message_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE SET NULL,

    CONSTRAINT fk_chat_message_group
        FOREIGN KEY (group_id)
            REFERENCES groups (id)
            ON DELETE CASCADE
);