-- Seed data for demo (M3)

INSERT INTO users(user_id, username, email, role) VALUES
    (1, 'marcus_candidate', 'candidate1@techstride.dev', 'Candidate'),
    (2, 'alex_candidate',   'candidate2@techstride.dev', 'Candidate'),
    (10, 'coach_sam',       'sam@techstride.dev', 'Coach'),
    (11, 'coach_kim',       'kim@techstride.dev', 'Coach');

INSERT INTO coaches(coach_id, user_id, specialty, rating) VALUES
    (101, 10, 'System Design', 4.8),
    (102, 11, 'Java/Spring', 4.7);

INSERT INTO services(service_id, service_name, price) VALUES
    (1, 'Mock Interview', 149.00),
    (2, 'Certification Coaching', 99.00);

INSERT INTO availability_slots(slot_id, coach_id, start_time, end_time, status, version) VALUES
    (1001, 101, '2026-03-10 10:00:00', '2026-03-10 11:00:00', 'Available', 0),
    (1002, 101, '2026-03-10 11:00:00', '2026-03-10 12:00:00', 'Available', 0),
    (2001, 102, '2026-03-11 13:00:00', '2026-03-11 14:00:00', 'Available', 0);
