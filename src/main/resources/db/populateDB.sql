DELETE
FROM user_roles;
DELETE
FROM meals;
DELETE
FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (user_id, datetime, description, calories)
VALUES (100000, '2022-10-24 09:00:00'::timestamp, 'Завтрак',                      500),
       (100000, '2022-10-26 12:00:00'::timestamp, 'Обед',                         500),
       (100000, '2022-10-26 19:00:00'::timestamp, 'Ужин',                         500),
       (100001, '2022-10-31 00:00:00'::timestamp, 'Еда на граничное значение',    500),
       (100001, '2022-10-24 09:00:00'::timestamp, 'Завтрак',                      2000),
       (100001, '2022-10-24 12:00:00'::timestamp, 'Обед',                         500),
       (100002, '2022-10-24 12:00:00'::timestamp, 'Перекус',                      500);