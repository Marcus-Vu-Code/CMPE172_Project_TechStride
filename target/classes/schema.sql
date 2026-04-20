-- CMPE 172 Term Project (M3) - TechStride Interview & Certification Scheduler
-- SQLite schema (JDBC, no ORM)

DROP TABLE IF EXISTS appointments;
DROP TABLE IF EXISTS availability_slots;
DROP TABLE IF EXISTS services;
DROP TABLE IF EXISTS coaches;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    user_id     INTEGER PRIMARY KEY,
    username    TEXT NOT NULL,
    email       TEXT NOT NULL,
    role        TEXT NOT NULL
);

CREATE TABLE coaches (
    coach_id    INTEGER PRIMARY KEY,
    user_id     INTEGER NOT NULL,
    specialty   TEXT,
    rating      REAL,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE services (
    service_id  INTEGER PRIMARY KEY,
    service_name TEXT NOT NULL,
    price       REAL
);

CREATE TABLE availability_slots (
    slot_id     INTEGER PRIMARY KEY,
    coach_id    INTEGER NOT NULL,
    start_time  TIMESTAMP NOT NULL,
    end_time    TIMESTAMP NOT NULL,
    status      TEXT NOT NULL,
    version     INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY (coach_id) REFERENCES coaches(coach_id)
);

CREATE TABLE appointments (
    appointment_id INTEGER PRIMARY KEY AUTOINCREMENT,
    candidate_id   INTEGER NOT NULL,
    slot_id        INTEGER NOT NULL UNIQUE,
    service_id     INTEGER NOT NULL,
    booking_date   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (candidate_id) REFERENCES users(user_id),
    FOREIGN KEY (slot_id) REFERENCES availability_slots(slot_id),
    FOREIGN KEY (service_id) REFERENCES services(service_id)
);
