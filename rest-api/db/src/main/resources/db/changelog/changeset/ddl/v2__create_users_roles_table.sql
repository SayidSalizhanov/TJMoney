CREATE TABLE users_roles (
                             user_id BIGINT NOT NULL,
                             role_id BIGINT NOT NULL,
    ----------------------------------------------------------------------
                             CONSTRAINT pk_user_roles        PRIMARY KEY (user_id, role_id),
                             CONSTRAINT fk_user_roles_users  FOREIGN KEY (user_id)               REFERENCES users(id),
                             CONSTRAINT fk_user_roles_roles  FOREIGN KEY (role_id)               REFERENCES roles(id)
);

COMMENT ON TABLE users_roles              IS 'Пользователи и их роли';
COMMENT ON COLUMN users_roles.user_id     IS 'Идентификатор пользователя';
COMMENT ON COLUMN users_roles.role_id     IS 'Идентификатор роли';

--rollback DROP TABLE users_roles;

