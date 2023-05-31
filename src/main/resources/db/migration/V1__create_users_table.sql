CREATE TABLE USERS (
    id bigserial PRIMARY KEY,
    username varchar(50) UNIQUE NOT NULL,
    email varchar(50) NOT NULL,
    password text NOT NULL
);