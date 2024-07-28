drop table if exists users cascade;
drop table if exists clothes cascade;
drop table if exists categories cascade;


CREATE TABLE IF NOT EXISTS users
(
    id       INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name     VARCHAR(16)        NOT NULL,
    email    VARCHAR(32) UNIQUE NOT NULL,
    password VARCHAR(1024),
    address  VARCHAR(4096),
    role     VARCHAR(16)
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
    FOREIGN KEY (category_id) REFERENCES categories (id),
    FOREIGN KEY (owner_id) REFERENCES users (id)
);
