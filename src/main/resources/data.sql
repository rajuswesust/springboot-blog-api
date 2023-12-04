INSERT INTO `roles`(`name`) VALUES ('SUPER ADMIN'), ('ADMIN'), ('USER');

INSERT INTO `users`(`email`, `name`, `password`, `username`, `is_enabled`)
VALUES ('singhoraju8@gmail.com', 'raju singho', '$2a$10$9mb3Hcz2VU7WCB/aGoJHAOc8igVHlpOcPo708lM6K/6IsAAY4L0u6', 'rajux', 1),
        ('nahidswe@gmail.com', 'lovon', '$2a$10$9mb3Hcz2VU7WCB/aGoJHAOc8igVHlpOcPo708lM6K/6IsAAY4L0u6', 'nahidx', 1),
        ('mahmudur69@student.sust.edu', 'raju singho', '$2a$10$9mb3Hcz2VU7WCB/aGoJHAOc8igVHlpOcPo708lM6K/6IsAAY4L0u6', 'mahmudx', 1);