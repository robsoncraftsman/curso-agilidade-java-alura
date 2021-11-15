CREATE TABLE users (
    id BIGSERIAL,
    name VARCHAR(255) NOT NULL ,
    email VARCHAR(255) NOT NULL ,
    password VARCHAR(255) NOT NULL ,

    CONSTRAINT pk_users PRIMARY KEY (id)
);