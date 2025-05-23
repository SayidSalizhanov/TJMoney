CREATE TABLE users
(
    id                  BIGSERIAL PRIMARY KEY,
    username            VARCHAR UNIQUE,
    email               VARCHAR UNIQUE,
    password            VARCHAR,
    telegram_id         VARCHAR,
    sending_to_telegram BOOLEAN,
    sending_to_email    BOOLEAN
);