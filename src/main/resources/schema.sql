drop table if exists user_role cascade;
drop table if exists roles cascade;
drop table if exists clothes cascade;
drop table if exists users cascade;
drop table if exists categories cascade;


CREATE TABLE IF NOT EXISTS users
(
    id       INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    username VARCHAR(16) UNIQUE NOT NULL,
    email    VARCHAR(32) UNIQUE NOT NULL,
    password VARCHAR(1024),
    address  VARCHAR(4096),
    role     VARCHAR(32)
);

CREATE TABLE IF NOT EXISTS roles
(
    id    INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    roles VARCHAR(32)
);

CREATE TABLE IF NOT EXISTS user_role
(
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS categories
(
    id          INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name        VARCHAR(32) NOT NULL,
    description VARCHAR(1024)
);

CREATE TABLE IF NOT EXISTS clothes
(
    id          INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name        VARCHAR(32),
    description VARCHAR(128),
    price       DECIMAL(10, 2),
    category_id INT,
    owner_id    INT,
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP,
    color       VARCHAR(50),
    size        VARCHAR(50),
    material    VARCHAR(50),
    is_sold     boolean,
    gender      VARCHAR(16),

    FOREIGN KEY (category_id) REFERENCES categories (id),
    FOREIGN KEY (owner_id) REFERENCES users (id)
);