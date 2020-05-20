CREATE TABLE IF NOT EXISTS auction (
    id SERIAL PRIMARY KEY,
    owner_id INTEGER NOT NULL REFERENCES users(id),
    category_id INTEGER NOT NULL REFERENCES category(id),
    sub_category_id INTEGER NOT NULL REFERENCES sub_category(id),
    title VARCHAR(50) NOT NULL,
    description VARCHAR(5000) NOT NULL,
    price FLOAT NOT NULL,
    quantity INTEGER NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL
);