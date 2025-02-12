create table product(
    id SERIAL primary key,
    name VARCHAR(255) not null unique
);

INSERT INTO product (name) values
('Milk'),
('Coffee'),
('Tea'),
('Water')
;