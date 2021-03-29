CREATE TABLE IF NOT EXISTS posts (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    description TEXT,
    created VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS candidates (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255)
);