use db;

CREATE TABLE IF NOT EXISTS store (
    store_id VARCHAR(255) NOT NULL PRIMARY KEY,
    store_latitude DOUBLE NOT NULL,
    store_longitude DOUBLE NOT NULL
);

CREATE TABLE IF NOT EXISTS driver (
    driver_id VARCHAR(255) NOT NULL PRIMARY KEY,
    driver_latitude DOUBLE NOT NULL,
    driver_longitude DOUBLE NOT NULL
);