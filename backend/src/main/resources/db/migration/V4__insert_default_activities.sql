
ALTER TABLE activities DROP COLUMN user_id;

ALTER TABLE activities RENAME COLUMN unit_name TO measurement_unit;

DELETE FROM activities;

INSERT INTO activities (name, points_multiplier, measurement_unit) VALUES
('Reading Protocol', 10, 'pages'),
('Physical Training', 15, 'minutes'),
('Deep Work Session', 20, 'pomodoros'),
('Code Commits', 25, 'commits');

UPDATE users SET role = 'ADMIN' WHERE user_id = 1;
