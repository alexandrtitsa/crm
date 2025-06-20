-- === Додати роль ADMIN, якщо ще не існує ===
INSERT INTO roles (name)
SELECT 'ROLE_ADMIN'
    WHERE NOT EXISTS (
    SELECT 1 FROM roles WHERE name = 'ROLE_ADMIN'
);

-- === Створити користувача-адміністратора з BCrypt-паролем ===
-- Пароль: Admin123
INSERT INTO users (email, password, full_name, enabled)
VALUES (
           'admin@acrm.com',
           '$2a$10$EqRr23sBFe8Z7GVRkhuP4ehxFu6NL6aVKsg60jR0AJ66nFUtqjTxO',
           'System Administrator',
           true
       );

-- === Призначити роль ADMIN новому користувачу ===
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
         JOIN roles r ON r.name = 'ROLE_ADMIN'
WHERE u.email = 'admin@acrm.com';
