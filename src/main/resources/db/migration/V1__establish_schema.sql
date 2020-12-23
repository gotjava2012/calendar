-- All *.sql files living under db/migration will be automatically applied by Flyway.
-- This V1 file establishes the initial schema of the database.

-- Allow for the addition of future statuses
CREATE TABLE statuses (
  id SERIAL PRIMARY KEY,
  status TEXT NOT NULL
);

CREATE TABLE appointments (

  -- The PK of the appointments table
  id SERIAL PRIMARY KEY,

  -- The creation timestamp of the row
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  -- The timestamp of the appointment, which will be stored in UTC
  appointment_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  -- The duration of the appointment, in minutes
  appointment_duration INTEGER NOT NULL,

  -- Foreign key to the Statuses table
  status_id INTEGER REFERENCES statuses(id)

);

-- Create our two initial statuses
INSERT INTO statuses (status)
VALUES
  ('Available'),
  ('Booked');
