INSERT INTO user_roles ("user_id", "role_id")
VALUES ((SELECT id FROM users WHERE email = 'admin@example.com'),
        (SELECT id FROM roles WHERE name = 'USER')),
       ((SELECT id FROM users WHERE email = 'admin@example.com'),
        (SELECT id FROM roles WHERE name = 'SALESMAN')),
       ((SELECT id FROM users WHERE email = 'admin@example.com'),
        (SELECT id FROM roles WHERE name = 'ADMIN'));