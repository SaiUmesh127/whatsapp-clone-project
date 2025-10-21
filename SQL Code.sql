CREATE DATABASE whatsapp_db;
SHOW DATABASES;
USE whatsapp_db;

-- Check all users
SELECT * FROM users;

-- Check all messages
SELECT * FROM messages;

-- Check login attempts
SELECT * FROM login_attempts;

-- Check conversation between two users
SELECT 
    m.id,
    s.username as sender,
    r.username as receiver,
    m.content,
    m.sent_at,
    m.is_read
FROM messages m
JOIN users s ON m.sender_id = s.id
JOIN users r ON m.receiver_id = r.id
ORDER BY m.sent_at;

-- Check user's online status
SELECT username, full_name, online, last_seen 
FROM users;

-- Count messages per user
SELECT 
    u.username,
    COUNT(m.id) as total_messages
FROM users u
LEFT JOIN messages m ON u.id = m.sender_id
GROUP BY u.id, u.username;

SELECT id, username, email, full_name, created_at FROM users ORDER BY id DESC LIMIT 5;

-- Check user status updated
SELECT username, online, last_seen FROM users WHERE username = 'Umesh';

-- Check login attempt logged
SELECT * FROM login_attempts ORDER BY attempted_at DESC LIMIT 1;