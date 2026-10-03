-- Campus Events seed data
-- Every statement is guarded (INSERT ... WHERE NOT EXISTS / AND EXISTS),
-- so re-running on an existing database changes nothing.
-- ASCII only in this file. Add new rows by copying a block.

-- Venues feed the organiser event form dropdown (GET /venue).
INSERT INTO venues (name, capacity, street, suburb, city, province)
SELECT 'Major Hall', 500, '1 Hanover Street', 'District Six', 'Cape Town', 'Western Cape'
WHERE NOT EXISTS (SELECT 1 FROM venues WHERE name = 'Major Hall');

INSERT INTO venues (name, capacity, street, suburb, city, province)
SELECT 'Library Auditorium', 200, '1 Hanover Street', 'District Six', 'Cape Town', 'Western Cape'
WHERE NOT EXISTS (SELECT 1 FROM venues WHERE name = 'Library Auditorium');

INSERT INTO venues (name, capacity, street, suburb, city, province)
SELECT 'Engineering Lab 2.01', 60, 'Bellville Campus', 'Bellville', 'Cape Town', 'Western Cape'
WHERE NOT EXISTS (SELECT 1 FROM venues WHERE name = 'Engineering Lab 2.01');

INSERT INTO venues (name, capacity, street, suburb, city, province)
SELECT 'Sports Hall', 800, 'Bellville Campus', 'Bellville', 'Cape Town', 'Western Cape'
WHERE NOT EXISTS (SELECT 1 FROM venues WHERE name = 'Sports Hall');

-- Demo organisers for visual testing (password: password123).
-- Skipped when the faculty they belong to does not exist yet.
INSERT INTO organiser (first_name, last_name, email, password, role, faculty_id, created_at, active)
SELECT 'Tony', 'Organiser', 'tony.organiser@cput.ac.za', 'password123', 'ORGANISER',
       (SELECT id FROM faculty WHERE name = 'Faculty of Information & Communication Technology'),
       NOW(), TRUE
WHERE NOT EXISTS (SELECT 1 FROM organiser WHERE email = 'tony.organiser@cput.ac.za')
  AND EXISTS (SELECT 1 FROM faculty WHERE name = 'Faculty of Information & Communication Technology');

INSERT INTO organiser (first_name, last_name, email, password, role, faculty_id, created_at, active)
SELECT 'Demo', 'Organiser', 'organiser2@cput.ac.za', 'password123', 'ORGANISER',
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering'),
       NOW(), TRUE
WHERE NOT EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND EXISTS (SELECT 1 FROM faculty WHERE name = 'Faculty of Engineering');

-- A starter inbox so the organiser notifications panel shows real rows on first login.
-- Keyed off tony's id looked up by email, so fresh databases get the right owner.
-- NOTE: notifications.title is legacy NOT NULL cruft from an earlier design
-- (the entity no longer maps it), so seed rows must supply it.
INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Welcome', 'Welcome to Campus Events - your organiser account is active.', FALSE, NOW(),
       (SELECT id FROM organiser WHERE email = 'tony.organiser@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'tony.organiser@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id =
       (SELECT id FROM organiser WHERE email = 'tony.organiser@cput.ac.za')
       AND message = 'Welcome to Campus Events - your organiser account is active.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Tip', 'Tip: create your first event from My Events, then close registration when it fills up.', FALSE, NOW(),
       (SELECT id FROM organiser WHERE email = 'tony.organiser@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'tony.organiser@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id =
       (SELECT id FROM organiser WHERE email = 'tony.organiser@cput.ac.za')
       AND message = 'Tip: create your first event from My Events, then close registration when it fills up.');
