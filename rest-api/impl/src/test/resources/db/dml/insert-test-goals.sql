INSERT INTO goals (user_id, group_id, title, description, progress)
SELECT id, NULL, 'Goal #1', 'Description for goal #1', 10
FROM users WHERE username = 'testuser';

INSERT INTO goals (user_id, group_id, title, description, progress)
SELECT id, NULL, 'Goal #2', 'Description for goal #2', 20
FROM users WHERE username = 'testuser';

INSERT INTO goals (user_id, group_id, title, description, progress)
SELECT id, NULL, 'Goal #3', 'Description for goal #3', 30
FROM users WHERE username = 'testuser';

INSERT INTO goals (user_id, group_id, title, description, progress)
SELECT id, NULL, 'Goal #4', 'Description for goal #4', 40
FROM users WHERE username = 'testuser';

INSERT INTO goals (user_id, group_id, title, description, progress)
SELECT id, NULL, 'Goal #5', 'Description for goal #5', 50
FROM users WHERE username = 'testuser';

INSERT INTO goals (user_id, group_id, title, description, progress)
SELECT id, NULL, 'Goal #6', 'Description for goal #6', 60
FROM users WHERE username = 'testuser';

INSERT INTO goals (user_id, group_id, title, description, progress)
SELECT id, NULL, 'Goal #7', 'Description for goal #7', 70
FROM users WHERE username = 'testuser';

INSERT INTO goals (user_id, group_id, title, description, progress)
SELECT id, NULL, 'Goal #8', 'Description for goal #8', 80
FROM users WHERE username = 'testuser';

INSERT INTO goals (user_id, group_id, title, description, progress)
SELECT id, NULL, 'Goal #9', 'Description for goal #9', 90
FROM users WHERE username = 'testuser';

INSERT INTO goals (user_id, group_id, title, description, progress)
SELECT id, NULL, 'Goal #10', 'Description for goal #10', 100
FROM users WHERE username = 'testuser';