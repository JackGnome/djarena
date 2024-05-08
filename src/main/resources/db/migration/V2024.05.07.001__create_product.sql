CREATE TABLE product (
    id character varying(10) PRIMARY KEY,
    name character varying(255) NOT NULL,
    price NUMERIC(10, 4),
    preview character varying(1024) NOT NULL
);