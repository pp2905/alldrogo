CREATE TABLE IF NOT EXISTS Users (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(30) NOT NULL,
    last_name VARCHAR(30) NOT NULL,
    email VARCHAR(40) NOT NULL UNIQUE,
    phone_number INTEGER,
    birth_date DATE NOT NULL,
    street VARCHAR(30),
    house_number INTEGER,
    flat_number INTEGER,
    post_code VARCHAR(10),
    post_office VARCHAR(30)
);