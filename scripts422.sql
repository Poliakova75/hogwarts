CREATE TABLE Person (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT CHECK (age >= 0),
    has_license BOOLEAN NOT NULL
);

CREATE TABLE Car (
    id SERIAL PRIMARY KEY,
    brand VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL
);

CREATE TABLE PersonCar (
    person_id INT REFERENCES Person(id),
    car_id INT REFERENCES Car(id),
    PRIMARY KEY (person_id, car_id)  -- Уникальная пара person_id и car_id
);