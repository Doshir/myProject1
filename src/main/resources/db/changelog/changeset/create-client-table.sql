CREATE TABLE IF NOT EXISTS clients (
                                       client_id SERIAL PRIMARY KEY,
                                       first_name VARCHAR(255),
    last_name VARCHAR(255),
    middle_name VARCHAR(255),
    birthday TIMESTAMP WITHOUT TIME ZONE,
    birth_place VARCHAR(255)
    );