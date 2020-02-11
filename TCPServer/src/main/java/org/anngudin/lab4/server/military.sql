\connect postgres;
DROP DATABASE IF EXISTS military;
CREATE DATABASE military;
CREATE ROLE IF NOT EXISTS `military` WITH ADMIN;
\connect DATABASE military;

CREATE TABLE clients (
    id serial PRIMARY KEY,
    TIN varchar (255) NOT NULL, /*Taxpayer Identification Numbr*/
    country varchar (255) NOT NULL
);

INSERT INTO clients
    (TIN, country)
VALUES     
    ('394767437', 'Canada'),
    ('927483672', 'USA'),
    ('264735321', 'Russia');

CREATE TABLE orders (
    id serial PRIMARY KEY,
    type varchar (255) NOT NULL,
    subtype varchar (1024) NOT NULL,
    marking varchar (255) NOT NULL,
    client_id varchar(255) NOT NULL
    -- CONSTRAINT fk_client_id FOREIGN KEY (client_id) REFERENCES clients (id)
);  

INSERT INTO orders
    (type, subtype, marking, client_id)
VALUES
    ('Army Equipment', 'Carbine', 'M4', 1), 
    ('Navy Equipment', 'Rifle', 'M16A2', 2), 
    ('Air Force Equipment', 'Spirit', 'B-2', 3);