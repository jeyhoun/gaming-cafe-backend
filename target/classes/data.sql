INSERT INTO users (id, email, username, password, balance, is_active, created_at)
VALUES (1, 'alice@example.com', 'alice', 'pass123',  50.00, TRUE, NOW()),
       (2, 'bob@example.com', 'bob', 'pass123',  30.00, TRUE, NOW()),
       (3, 'admin@example.com', 'admin', 'adminpass', 0.00, TRUE, NOW());

INSERT INTO computers (name, status, ip_address, specs, created_at)
VALUES ('PC-01', 'AVAILABLE', '192.168.1.101', '{
  "cpu": "Intel i7-12700",
  "gpu": "RTX 3060",
  "ram": "16GB"
}', NOW()),
       ('PC-02', 'AVAILABLE', '192.168.1.102', '{
         "cpu": "Intel i7-12700",
         "gpu": "RTX 3060",
         "ram": "16GB"
       }', NOW()),
       ('PC-03', 'AVAILABLE', '192.168.1.103', '{
         "cpu": "Intel i7-12700",
         "gpu": "RTX 3060",
         "ram": "16GB"
       }', NOW()),
       ('PC-04', 'AVAILABLE', '192.168.1.104', '{
         "cpu": "Intel i7-12700",
         "gpu": "RTX 3060",
         "ram": "16GB"
       }', NOW()),
       ('PC-05', 'AVAILABLE', '192.168.1.105', '{
         "cpu": "Intel i7-12700",
         "gpu": "RTX 3060",
         "ram": "16GB"
       }', NOW()),
       ('VIP-01', 'AVAILABLE', '192.168.1.201', '{
         "cpu": "Intel i9-13900K",
         "gpu": "RTX 4090",
         "ram": "32GB"
       }', NOW()),
       ('VIP-02', 'AVAILABLE', '192.168.1.202', '{
         "cpu": "Intel i9-13900K",
         "gpu": "RTX 4090",
         "ram": "32GB"
       }', NOW()),
       ('VIP-03', 'AVAILABLE', '192.168.1.203', '{
         "cpu": "Intel i9-13900K",
         "gpu": "RTX 4090",
         "ram": "32GB"
       }', NOW()),
       ('VIP-04', 'AVAILABLE', '192.168.1.204', '{
         "cpu": "Intel i9-13900K",
         "gpu": "RTX 4090",
         "ram": "32GB"
       }', NOW()),
       ('VIP-05', 'AVAILABLE', '192.168.1.205', '{
         "cpu": "Intel i9-13900K",
         "gpu": "RTX 4090",
         "ram": "32GB"
       }', NOW());

INSERT INTO sessions (id, user_id, computer_id, start_time, end_time, total_minutes, total_cost, status, created_at)
VALUES (1, 1, 1, '2026-02-09 10:00:00', '2026-02-09 11:30:00', 90, 7.50, 'COMPLETED', NOW()),
       (2, 2, 2, '2026-02-09 12:00:00', NULL, NULL, NULL, 'ACTIVE', NOW()),
       (3, 1, 3, '2026-02-08 20:00:00', '2026-02-08 22:00:00', 120, 7.00, 'COMPLETED', NOW());

INSERT INTO prices (name, price_per_hour, start_time, end_time, days_of_week, is_active)
VALUES ('Standart Saat', 2.00, '09:00', '22:00', ARRAY [1, 2, 3, 4, 5, 6, 7], true),
       ('Happy Hour', 1.50, '14:00', '18:00', ARRAY [1, 2, 3, 4, 5], true),
       ('Gecə Paketi', 3.00, '22:00', '06:00', ARRAY [6, 7], true);

INSERT INTO payments (id, user_id, session_id, amount, payment_type, created_at)
VALUES (1, 1, 1, 7.50, 'CARD', NOW()),
       (2, 2, NULL, 20.00, 'BALANCE_LOAD', NOW()),
       (3, 1, 3, 7.00, 'CASH', NOW());

INSERT INTO roles (id, name)
VALUES (1, 'USER'),
       (2, 'ADMIN'),
       (3, 'MODERATOR'),
       (4, 'SUPPORT'),
       (5, 'DEVOPS'),
       (6, 'BACKEND');