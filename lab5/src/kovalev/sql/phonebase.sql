DROP USER IF EXISTS pbuser;
DROP DATABASE IF EXISTS phonedb;

CREATE USER pbuser WITH PASSWORD 'pbpwd';
CREATE DATABASE phonedb;
GRANT ALL PRIVILEGES ON DATABASE phonedb TO pbuser;

\c phonedb

CREATE TABLE PhoneBook(
    ContactID SERIAL PRIMARY KEY,
    FirstName varchar(50) not null,
    LastName varchar(50) not null,
    Phone varchar(50) not null,
    EMail varchar(50)
);