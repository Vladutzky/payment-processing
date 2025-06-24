INSERT INTO users (username, password, enabled) VALUES
    ('admin', '$2a$10$R/d6dS2KZCdR7UKj/YgO6OyzsJQl0QvO54yPZrWobL5kHrPuqRJo6', true); -- parola este "admin"

INSERT INTO authorities (username, authority) VALUES
    ('admin', 'ROLE_ADMIN');
