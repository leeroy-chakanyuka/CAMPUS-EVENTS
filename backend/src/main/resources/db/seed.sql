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

-- Legacy mirror: the event.venue_id FK still points at the old `venue` table
-- while the app reads from `venues`. Mirror the same four rows (same ids)
-- until the FK is migrated. Unify the tables instead of extending this.
INSERT INTO venue (name, capacity, street, suburb, city, province)
SELECT 'Major Hall', 500, '1 Hanover Street', 'District Six', 'Cape Town', 'Western Cape'
WHERE NOT EXISTS (SELECT 1 FROM venue WHERE name = 'Major Hall');

INSERT INTO venue (name, capacity, street, suburb, city, province)
SELECT 'Library Auditorium', 200, '1 Hanover Street', 'District Six', 'Cape Town', 'Western Cape'
WHERE NOT EXISTS (SELECT 1 FROM venue WHERE name = 'Library Auditorium');

INSERT INTO venue (name, capacity, street, suburb, city, province)
SELECT 'Engineering Lab 2.01', 60, 'Bellville Campus', 'Bellville', 'Cape Town', 'Western Cape'
WHERE NOT EXISTS (SELECT 1 FROM venue WHERE name = 'Engineering Lab 2.01');

INSERT INTO venue (name, capacity, street, suburb, city, province)
SELECT 'Sports Hall', 800, 'Bellville Campus', 'Bellville', 'Cape Town', 'Western Cape'
WHERE NOT EXISTS (SELECT 1 FROM venue WHERE name = 'Sports Hall');

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

-- Bulk visual-test data for organiser2 (60 events + 60 inbox rows). Same guards: reruns change nothing.
INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 01', 'Seed event 01 for visual testing.', DATE_ADD(NOW(), INTERVAL -4 DAY), 27, FALSE, NOW(), 1,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 01' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 02', 'Seed event 02 for visual testing.', DATE_ADD(NOW(), INTERVAL -3 DAY), 34, TRUE, NOW(), 2,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 02' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 03', 'Seed event 03 for visual testing.', DATE_ADD(NOW(), INTERVAL -2 DAY), 41, FALSE, NOW(), 3,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 03' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 04', 'Seed event 04 for visual testing.', DATE_ADD(NOW(), INTERVAL -1 DAY), 48, TRUE, NOW(), 4,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 04' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 05', 'Seed event 05 for visual testing.', DATE_ADD(NOW(), INTERVAL 0 DAY), 55, FALSE, NOW(), 1,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 05' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 06', 'Seed event 06 for visual testing.', DATE_ADD(NOW(), INTERVAL 1 DAY), 22, TRUE, NOW(), 2,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 06' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 07', 'Seed event 07 for visual testing.', DATE_ADD(NOW(), INTERVAL 2 DAY), 29, FALSE, NOW(), 3,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 07' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 08', 'Seed event 08 for visual testing.', DATE_ADD(NOW(), INTERVAL 3 DAY), 36, TRUE, NOW(), 4,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 08' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 09', 'Seed event 09 for visual testing.', DATE_ADD(NOW(), INTERVAL 4 DAY), 43, FALSE, NOW(), 1,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 09' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 10', 'Seed event 10 for visual testing.', DATE_ADD(NOW(), INTERVAL 5 DAY), 50, TRUE, NOW(), 2,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 10' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 11', 'Seed event 11 for visual testing.', DATE_ADD(NOW(), INTERVAL 6 DAY), 57, FALSE, NOW(), 3,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 11' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 12', 'Seed event 12 for visual testing.', DATE_ADD(NOW(), INTERVAL 7 DAY), 24, TRUE, NOW(), 4,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 12' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 13', 'Seed event 13 for visual testing.', DATE_ADD(NOW(), INTERVAL 8 DAY), 31, FALSE, NOW(), 1,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 13' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 14', 'Seed event 14 for visual testing.', DATE_ADD(NOW(), INTERVAL 9 DAY), 38, TRUE, NOW(), 2,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 14' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 15', 'Seed event 15 for visual testing.', DATE_ADD(NOW(), INTERVAL 10 DAY), 45, FALSE, NOW(), 3,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 15' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 16', 'Seed event 16 for visual testing.', DATE_ADD(NOW(), INTERVAL 11 DAY), 52, TRUE, NOW(), 4,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 16' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 17', 'Seed event 17 for visual testing.', DATE_ADD(NOW(), INTERVAL 12 DAY), 59, FALSE, NOW(), 1,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 17' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 18', 'Seed event 18 for visual testing.', DATE_ADD(NOW(), INTERVAL 13 DAY), 26, TRUE, NOW(), 2,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 18' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 19', 'Seed event 19 for visual testing.', DATE_ADD(NOW(), INTERVAL 14 DAY), 33, FALSE, NOW(), 3,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 19' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 20', 'Seed event 20 for visual testing.', DATE_ADD(NOW(), INTERVAL 15 DAY), 40, TRUE, NOW(), 4,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 20' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 21', 'Seed event 21 for visual testing.', DATE_ADD(NOW(), INTERVAL 16 DAY), 47, FALSE, NOW(), 1,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 21' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 22', 'Seed event 22 for visual testing.', DATE_ADD(NOW(), INTERVAL 17 DAY), 54, TRUE, NOW(), 2,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 22' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 23', 'Seed event 23 for visual testing.', DATE_ADD(NOW(), INTERVAL 18 DAY), 21, FALSE, NOW(), 3,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 23' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 24', 'Seed event 24 for visual testing.', DATE_ADD(NOW(), INTERVAL 19 DAY), 28, TRUE, NOW(), 4,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 24' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 25', 'Seed event 25 for visual testing.', DATE_ADD(NOW(), INTERVAL 20 DAY), 35, FALSE, NOW(), 1,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 25' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 26', 'Seed event 26 for visual testing.', DATE_ADD(NOW(), INTERVAL 21 DAY), 42, TRUE, NOW(), 2,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 26' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 27', 'Seed event 27 for visual testing.', DATE_ADD(NOW(), INTERVAL 22 DAY), 49, FALSE, NOW(), 3,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 27' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 28', 'Seed event 28 for visual testing.', DATE_ADD(NOW(), INTERVAL 23 DAY), 56, TRUE, NOW(), 4,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 28' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 29', 'Seed event 29 for visual testing.', DATE_ADD(NOW(), INTERVAL 24 DAY), 23, FALSE, NOW(), 1,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 29' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 30', 'Seed event 30 for visual testing.', DATE_ADD(NOW(), INTERVAL -5 DAY), 30, TRUE, NOW(), 2,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 30' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 31', 'Seed event 31 for visual testing.', DATE_ADD(NOW(), INTERVAL -4 DAY), 37, FALSE, NOW(), 3,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 31' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 32', 'Seed event 32 for visual testing.', DATE_ADD(NOW(), INTERVAL -3 DAY), 44, TRUE, NOW(), 4,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 32' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 33', 'Seed event 33 for visual testing.', DATE_ADD(NOW(), INTERVAL -2 DAY), 51, FALSE, NOW(), 1,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 33' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 34', 'Seed event 34 for visual testing.', DATE_ADD(NOW(), INTERVAL -1 DAY), 58, TRUE, NOW(), 2,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 34' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 35', 'Seed event 35 for visual testing.', DATE_ADD(NOW(), INTERVAL 0 DAY), 25, FALSE, NOW(), 3,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 35' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 36', 'Seed event 36 for visual testing.', DATE_ADD(NOW(), INTERVAL 1 DAY), 32, TRUE, NOW(), 4,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 36' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 37', 'Seed event 37 for visual testing.', DATE_ADD(NOW(), INTERVAL 2 DAY), 39, FALSE, NOW(), 1,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 37' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 38', 'Seed event 38 for visual testing.', DATE_ADD(NOW(), INTERVAL 3 DAY), 46, TRUE, NOW(), 2,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 38' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 39', 'Seed event 39 for visual testing.', DATE_ADD(NOW(), INTERVAL 4 DAY), 53, FALSE, NOW(), 3,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 39' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 40', 'Seed event 40 for visual testing.', DATE_ADD(NOW(), INTERVAL 5 DAY), 20, TRUE, NOW(), 4,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 40' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 41', 'Seed event 41 for visual testing.', DATE_ADD(NOW(), INTERVAL 6 DAY), 27, FALSE, NOW(), 1,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 41' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 42', 'Seed event 42 for visual testing.', DATE_ADD(NOW(), INTERVAL 7 DAY), 34, TRUE, NOW(), 2,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 42' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 43', 'Seed event 43 for visual testing.', DATE_ADD(NOW(), INTERVAL 8 DAY), 41, FALSE, NOW(), 3,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 43' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 44', 'Seed event 44 for visual testing.', DATE_ADD(NOW(), INTERVAL 9 DAY), 48, TRUE, NOW(), 4,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 44' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 45', 'Seed event 45 for visual testing.', DATE_ADD(NOW(), INTERVAL 10 DAY), 55, FALSE, NOW(), 1,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 45' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 46', 'Seed event 46 for visual testing.', DATE_ADD(NOW(), INTERVAL 11 DAY), 22, TRUE, NOW(), 2,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 46' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 47', 'Seed event 47 for visual testing.', DATE_ADD(NOW(), INTERVAL 12 DAY), 29, FALSE, NOW(), 3,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 47' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 48', 'Seed event 48 for visual testing.', DATE_ADD(NOW(), INTERVAL 13 DAY), 36, TRUE, NOW(), 4,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 48' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 49', 'Seed event 49 for visual testing.', DATE_ADD(NOW(), INTERVAL 14 DAY), 43, FALSE, NOW(), 1,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 49' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 50', 'Seed event 50 for visual testing.', DATE_ADD(NOW(), INTERVAL 15 DAY), 50, TRUE, NOW(), 2,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 50' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 51', 'Seed event 51 for visual testing.', DATE_ADD(NOW(), INTERVAL 16 DAY), 57, FALSE, NOW(), 3,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 51' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 52', 'Seed event 52 for visual testing.', DATE_ADD(NOW(), INTERVAL 17 DAY), 24, TRUE, NOW(), 4,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 52' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 53', 'Seed event 53 for visual testing.', DATE_ADD(NOW(), INTERVAL 18 DAY), 31, FALSE, NOW(), 1,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 53' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 54', 'Seed event 54 for visual testing.', DATE_ADD(NOW(), INTERVAL 19 DAY), 38, TRUE, NOW(), 2,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 54' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 55', 'Seed event 55 for visual testing.', DATE_ADD(NOW(), INTERVAL 20 DAY), 45, FALSE, NOW(), 3,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 55' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 56', 'Seed event 56 for visual testing.', DATE_ADD(NOW(), INTERVAL 21 DAY), 52, TRUE, NOW(), 4,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 56' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 57', 'Seed event 57 for visual testing.', DATE_ADD(NOW(), INTERVAL 22 DAY), 59, FALSE, NOW(), 1,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 57' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 58', 'Seed event 58 for visual testing.', DATE_ADD(NOW(), INTERVAL 23 DAY), 26, TRUE, NOW(), 2,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 58' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 59', 'Seed event 59 for visual testing.', DATE_ADD(NOW(), INTERVAL 24 DAY), 33, FALSE, NOW(), 3,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 59' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO event (title, description, event_date, capacity, open, created_at, venue_id, organiser_id, faculty_id)
SELECT 'Seed Event 60', 'Seed event 60 for visual testing.', DATE_ADD(NOW(), INTERVAL -5 DAY), 40, TRUE, NOW(), 4,
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'),
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering')
WHERE NOT EXISTS (SELECT 1 FROM event WHERE title = 'Seed Event 60' AND organiser_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'))
  AND EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Seed Event 01 created - registration is open.', FALSE, DATE_SUB(NOW(), INTERVAL 59 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Seed Event 01 created - registration is open.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'New registration for Seed Event 02.', FALSE, DATE_SUB(NOW(), INTERVAL 58 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'New registration for Seed Event 02.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Registration closed for Seed Event 03.', TRUE, DATE_SUB(NOW(), INTERVAL 57 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Registration closed for Seed Event 03.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Reminder: Seed Event 04 is happening soon.', FALSE, DATE_SUB(NOW(), INTERVAL 56 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Reminder: Seed Event 04 is happening soon.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'A ticket for Seed Event 05 was cancelled.', FALSE, DATE_SUB(NOW(), INTERVAL 55 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'A ticket for Seed Event 05 was cancelled.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Seed Event 06 reached half capacity.', TRUE, DATE_SUB(NOW(), INTERVAL 54 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Seed Event 06 reached half capacity.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Seed Event 07 created - registration is open.', FALSE, DATE_SUB(NOW(), INTERVAL 53 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Seed Event 07 created - registration is open.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'New registration for Seed Event 08.', FALSE, DATE_SUB(NOW(), INTERVAL 52 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'New registration for Seed Event 08.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Registration closed for Seed Event 09.', TRUE, DATE_SUB(NOW(), INTERVAL 51 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Registration closed for Seed Event 09.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Reminder: Seed Event 10 is happening soon.', FALSE, DATE_SUB(NOW(), INTERVAL 50 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Reminder: Seed Event 10 is happening soon.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'A ticket for Seed Event 11 was cancelled.', FALSE, DATE_SUB(NOW(), INTERVAL 49 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'A ticket for Seed Event 11 was cancelled.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Seed Event 12 reached half capacity.', TRUE, DATE_SUB(NOW(), INTERVAL 48 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Seed Event 12 reached half capacity.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Seed Event 13 created - registration is open.', FALSE, DATE_SUB(NOW(), INTERVAL 47 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Seed Event 13 created - registration is open.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'New registration for Seed Event 14.', FALSE, DATE_SUB(NOW(), INTERVAL 46 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'New registration for Seed Event 14.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Registration closed for Seed Event 15.', TRUE, DATE_SUB(NOW(), INTERVAL 45 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Registration closed for Seed Event 15.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Reminder: Seed Event 16 is happening soon.', FALSE, DATE_SUB(NOW(), INTERVAL 44 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Reminder: Seed Event 16 is happening soon.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'A ticket for Seed Event 17 was cancelled.', FALSE, DATE_SUB(NOW(), INTERVAL 43 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'A ticket for Seed Event 17 was cancelled.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Seed Event 18 reached half capacity.', TRUE, DATE_SUB(NOW(), INTERVAL 42 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Seed Event 18 reached half capacity.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Seed Event 19 created - registration is open.', FALSE, DATE_SUB(NOW(), INTERVAL 41 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Seed Event 19 created - registration is open.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'New registration for Seed Event 20.', FALSE, DATE_SUB(NOW(), INTERVAL 40 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'New registration for Seed Event 20.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Registration closed for Seed Event 21.', TRUE, DATE_SUB(NOW(), INTERVAL 39 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Registration closed for Seed Event 21.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Reminder: Seed Event 22 is happening soon.', FALSE, DATE_SUB(NOW(), INTERVAL 38 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Reminder: Seed Event 22 is happening soon.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'A ticket for Seed Event 23 was cancelled.', FALSE, DATE_SUB(NOW(), INTERVAL 37 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'A ticket for Seed Event 23 was cancelled.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Seed Event 24 reached half capacity.', TRUE, DATE_SUB(NOW(), INTERVAL 36 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Seed Event 24 reached half capacity.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Seed Event 25 created - registration is open.', FALSE, DATE_SUB(NOW(), INTERVAL 35 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Seed Event 25 created - registration is open.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'New registration for Seed Event 26.', FALSE, DATE_SUB(NOW(), INTERVAL 34 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'New registration for Seed Event 26.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Registration closed for Seed Event 27.', TRUE, DATE_SUB(NOW(), INTERVAL 33 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Registration closed for Seed Event 27.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Reminder: Seed Event 28 is happening soon.', FALSE, DATE_SUB(NOW(), INTERVAL 32 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Reminder: Seed Event 28 is happening soon.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'A ticket for Seed Event 29 was cancelled.', FALSE, DATE_SUB(NOW(), INTERVAL 31 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'A ticket for Seed Event 29 was cancelled.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Seed Event 30 reached half capacity.', TRUE, DATE_SUB(NOW(), INTERVAL 30 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Seed Event 30 reached half capacity.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Seed Event 31 created - registration is open.', FALSE, DATE_SUB(NOW(), INTERVAL 29 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Seed Event 31 created - registration is open.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'New registration for Seed Event 32.', FALSE, DATE_SUB(NOW(), INTERVAL 28 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'New registration for Seed Event 32.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Registration closed for Seed Event 33.', TRUE, DATE_SUB(NOW(), INTERVAL 27 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Registration closed for Seed Event 33.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Reminder: Seed Event 34 is happening soon.', FALSE, DATE_SUB(NOW(), INTERVAL 26 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Reminder: Seed Event 34 is happening soon.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'A ticket for Seed Event 35 was cancelled.', FALSE, DATE_SUB(NOW(), INTERVAL 25 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'A ticket for Seed Event 35 was cancelled.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Seed Event 36 reached half capacity.', TRUE, DATE_SUB(NOW(), INTERVAL 24 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Seed Event 36 reached half capacity.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Seed Event 37 created - registration is open.', FALSE, DATE_SUB(NOW(), INTERVAL 23 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Seed Event 37 created - registration is open.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'New registration for Seed Event 38.', FALSE, DATE_SUB(NOW(), INTERVAL 22 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'New registration for Seed Event 38.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Registration closed for Seed Event 39.', TRUE, DATE_SUB(NOW(), INTERVAL 21 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Registration closed for Seed Event 39.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Reminder: Seed Event 40 is happening soon.', FALSE, DATE_SUB(NOW(), INTERVAL 20 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Reminder: Seed Event 40 is happening soon.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'A ticket for Seed Event 41 was cancelled.', FALSE, DATE_SUB(NOW(), INTERVAL 19 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'A ticket for Seed Event 41 was cancelled.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Seed Event 42 reached half capacity.', TRUE, DATE_SUB(NOW(), INTERVAL 18 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Seed Event 42 reached half capacity.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Seed Event 43 created - registration is open.', FALSE, DATE_SUB(NOW(), INTERVAL 17 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Seed Event 43 created - registration is open.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'New registration for Seed Event 44.', FALSE, DATE_SUB(NOW(), INTERVAL 16 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'New registration for Seed Event 44.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Registration closed for Seed Event 45.', TRUE, DATE_SUB(NOW(), INTERVAL 15 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Registration closed for Seed Event 45.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Reminder: Seed Event 46 is happening soon.', FALSE, DATE_SUB(NOW(), INTERVAL 14 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Reminder: Seed Event 46 is happening soon.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'A ticket for Seed Event 47 was cancelled.', FALSE, DATE_SUB(NOW(), INTERVAL 13 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'A ticket for Seed Event 47 was cancelled.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Seed Event 48 reached half capacity.', TRUE, DATE_SUB(NOW(), INTERVAL 12 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Seed Event 48 reached half capacity.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Seed Event 49 created - registration is open.', FALSE, DATE_SUB(NOW(), INTERVAL 11 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Seed Event 49 created - registration is open.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'New registration for Seed Event 50.', FALSE, DATE_SUB(NOW(), INTERVAL 10 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'New registration for Seed Event 50.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Registration closed for Seed Event 51.', TRUE, DATE_SUB(NOW(), INTERVAL 9 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Registration closed for Seed Event 51.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Reminder: Seed Event 52 is happening soon.', FALSE, DATE_SUB(NOW(), INTERVAL 8 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Reminder: Seed Event 52 is happening soon.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'A ticket for Seed Event 53 was cancelled.', FALSE, DATE_SUB(NOW(), INTERVAL 7 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'A ticket for Seed Event 53 was cancelled.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Seed Event 54 reached half capacity.', TRUE, DATE_SUB(NOW(), INTERVAL 6 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Seed Event 54 reached half capacity.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Seed Event 55 created - registration is open.', FALSE, DATE_SUB(NOW(), INTERVAL 5 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Seed Event 55 created - registration is open.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'New registration for Seed Event 56.', FALSE, DATE_SUB(NOW(), INTERVAL 4 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'New registration for Seed Event 56.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Registration closed for Seed Event 57.', TRUE, DATE_SUB(NOW(), INTERVAL 3 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Registration closed for Seed Event 57.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Reminder: Seed Event 58 is happening soon.', FALSE, DATE_SUB(NOW(), INTERVAL 2 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'Reminder: Seed Event 58 is happening soon.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'A ticket for Seed Event 59 was cancelled.', FALSE, DATE_SUB(NOW(), INTERVAL 1 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id = (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za') AND message = 'A ticket for Seed Event 59 was cancelled.');
INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Seed', 'Seed Event 60 reached half capacity.', TRUE, DATE_SUB(NOW(), INTERVAL 0 HOUR),
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za'), 'ORGANISER'
WHERE EXISTS (SELECT 1 FROM organiser WHERE email = 'organiser2@cput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id =
       (SELECT id FROM organiser WHERE email = 'organiser2@cput.ac.za')
       AND message = 'Seed Event 60 reached half capacity.');

-- Admin test login. e@gmail.com ships with NULL password, which NPEs login.
-- Only fills the blank; never overwrites a real password.
UPDATE admin SET password = 'password123' WHERE email = 'e@gmail.com' AND password IS NULL;

-- Pending organiser for the approval-flow visual test. The register/verify
-- path leaves new organisers inactive; approve via PUT /organiser/{id}/status,
-- then the account logs in fully and receives the approval notification.
INSERT INTO organiser (first_name, last_name, email, password, role, faculty_id, created_at, active)
SELECT 'Pending', 'Organiser', 'pending.organiser@cput.ac.za', 'password123', 'ORGANISER',
       (SELECT id FROM faculty WHERE name = 'Faculty of Engineering'),
       NOW(), FALSE
WHERE NOT EXISTS (SELECT 1 FROM organiser WHERE email = 'pending.organiser@cput.ac.za')
  AND EXISTS (SELECT 1 FROM faculty WHERE name = 'Faculty of Engineering');

-- Students for the admin StudentsPanel (suspend/reactivate) visual test.
-- Explicit ids: keeps reruns stable whether or not the column fix below
-- has been picked up yet.
INSERT INTO student (id, first_name, last_name, email, student_number, password, faculty_id, is_verified, active)
SELECT 1, 'Sipho', 'Nkosi', 'sipho@mycput.ac.za', '219012345', 'password123',
       (SELECT id FROM faculty WHERE name = 'Faculty of Information & Communication Technology'),
       TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM student WHERE email = 'sipho@mycput.ac.za')
  AND EXISTS (SELECT 1 FROM faculty WHERE name = 'Faculty of Information & Communication Technology');

INSERT INTO student (id, first_name, last_name, email, student_number, password, faculty_id, is_verified, active)
SELECT 2, 'Amahle', 'Dube', 'amahle@mycput.ac.za', '221098765', 'password123',
       (SELECT id FROM faculty WHERE name = 'Faculty of Business and Management Sciences'),
       TRUE, FALSE
WHERE NOT EXISTS (SELECT 1 FROM student WHERE email = 'amahle@mycput.ac.za')
  AND EXISTS (SELECT 1 FROM faculty WHERE name = 'Faculty of Business and Management Sciences');

-- student.id now auto-increments like every other entity (Student uses
-- IDENTITY since T0). The FKs block MODIFY, so drop, alter, re-add with the
-- same constraint names Hibernate generated. Re-runnable no-op on converged
-- DBs, which converges the rest of the team on their next boot.
ALTER TABLE notifications DROP FOREIGN KEY FKpavn8e1dwm8s42maj43hc5pjn;
ALTER TABLE ticket DROP FOREIGN KEY FK21tryhx6fi58vsfu5mgs0x2jr;
ALTER TABLE student MODIFY id BIGINT NOT NULL AUTO_INCREMENT;
ALTER TABLE ticket ADD CONSTRAINT FK21tryhx6fi58vsfu5mgs0x2jr FOREIGN KEY (student_id) REFERENCES student (id);
ALTER TABLE notifications ADD CONSTRAINT FKpavn8e1dwm8s42maj43hc5pjn FOREIGN KEY (student_id) REFERENCES student (id);

-- Starter inbox so the student notifications panel shows real rows on first login.
INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Welcome', 'Welcome to Campus Events - browse open events and get your first ticket.', FALSE, NOW(),
       (SELECT id FROM student WHERE email = 'sipho@mycput.ac.za'), 'STUDENT'
WHERE EXISTS (SELECT 1 FROM student WHERE email = 'sipho@mycput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id =
       (SELECT id FROM student WHERE email = 'sipho@mycput.ac.za')
       AND message = 'Welcome to Campus Events - browse open events and get your first ticket.');

INSERT INTO notifications (title, message, is_read, created_at, recipient_id, recipient_type)
SELECT 'Reminder', 'Your faculty posts new events every week - check Browse Events.', FALSE, NOW(),
       (SELECT id FROM student WHERE email = 'sipho@mycput.ac.za'), 'STUDENT'
WHERE EXISTS (SELECT 1 FROM student WHERE email = 'sipho@mycput.ac.za')
  AND NOT EXISTS (SELECT 1 FROM notifications WHERE recipient_id =
       (SELECT id FROM student WHERE email = 'sipho@mycput.ac.za')
       AND message = 'Your faculty posts new events every week - check Browse Events.');
