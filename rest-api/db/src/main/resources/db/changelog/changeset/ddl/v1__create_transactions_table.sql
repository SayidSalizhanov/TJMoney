CREATE TABLE transactions
(
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT,
    group_id    BIGINT,
    amount      INTEGER,
    category    VARCHAR,
    type        VARCHAR,
    date_time   TIMESTAMP,
    description VARCHAR,
    CONSTRAINT fk_transactions_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_transactions_group FOREIGN KEY (group_id) REFERENCES groups (id) ON DELETE CASCADE
);