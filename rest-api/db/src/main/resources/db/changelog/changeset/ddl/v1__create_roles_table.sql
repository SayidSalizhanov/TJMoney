CREATE SEQUENCE roles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

CREATE TABLE roles
(
    id   INTEGER PRIMARY KEY DEFAULT nextval('roles_id_seq'),
    name VARCHAR(20) UNIQUE NOT NULL
);


ALTER SEQUENCE roles_id_seq
    OWNED BY roles.id;

COMMENT
ON TABLE roles              IS 'Роли';
COMMENT
ON COLUMN roles.name        IS 'Уникальное имя роли (ROLE_USER, ROLE_ADMIN)';

--rollback DROP TABLE roles;