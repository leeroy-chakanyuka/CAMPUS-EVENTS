# Student Work Plan

Source: `Downloads/RESPONSIBILITY.md` (Leeroy owns the five student screens).
Promo is descoped from this pass — no promo field in student UI.

## Current state

Live: student register → verify → login, admin view of students.
Missing: everything a student does (login lands on a `JOptionPane`, no
dashboard, no browse/register/cancel path, no inbox, no ticket DTOs in Swing).

## Slices (backend first, one at a time)

- **T0 — De-risk `student.id` (spike, gates signup demos).**
  `Student` is the only entity on `GenerationType.AUTO` while its column has
  no auto-increment. App-side signup in `AuthService.verify()` risks id
  collisions with seeded ids 1/2. Decide IDENTITY vs sequence, migrate the
  column, prove live with a real register → verify minting id 3+.
- **T1 — Ticket endpoints (backend).** Expose `TicketService.issue()` via a
  `TicketController` (issue / my-tickets / cancel), add `TicketRepository`
  finders, thicken `TicketResponseDTO` (event + student fields).
- **T2 — Registration rules (backend).** Single home: `TicketService` owns
  (no double-register, no full/closed events, no double cancel).
  Extend `GET /event` with tickets-sold for the Spots-left column (DTO
  wire-in, no domain change).
- **T3 — Dashboard + Browse + Details (Swing).** Welcome + stat cards
  (upcoming count, tickets held, faculty); Browse table (Title, Faculty,
  Venue, Date, Spots left, Action) with search + faculty filter over
  `GET /event` + `GET /faculty`; Details (description, faculty badge, venue,
  date, capacity bar, Get Ticket, Back).
- **T4 — My Tickets + inbox (Swing).** Tickets table (Event, Date, Venue,
  Status) with cancel; receive-only inbox mirroring the organiser one;
  seed 1–2 rows for Sipho.
- **T5 — E2E + PR.** Full journey, suite green, grouped commits, PR up.

## Out of scope (others' areas — flag, don't fix)

`StudentFactory` TODOs, `FacultyFactory` reds, promo balance/deposit,
password hashing, Tony's stat-card placeholders.

## Test creds (`password123` unless noted)

- Admin: `e@gmail.com` (id 1).
- Organisers: `tony.organiser@cput.ac.za`, `organiser2@cput.ac.za`,
  `pending.organiser@cput.ac.za` (read-only).
- Students: `sipho@mycput.ac.za` (active), `amahle@mycput.ac.za` (suspended).
