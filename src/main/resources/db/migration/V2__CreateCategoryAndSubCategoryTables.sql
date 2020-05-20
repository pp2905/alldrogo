CREATE TABLE IF NOT EXISTS category (
    id SERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL,
    Description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS sub_category (
    id SERIAL PRIMARY KEY,
    category_id INTEGER NOT NULL REFERENCES category(id),
    name VARCHAR(30) NOT NULL,
    Description VARCHAR(255)
);