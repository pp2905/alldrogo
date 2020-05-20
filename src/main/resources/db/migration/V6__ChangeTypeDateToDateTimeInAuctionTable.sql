ALTER TABLE auction
    ALTER COLUMN start_date TYPE TIMESTAMP USING start_date,
    ALTER COLUMN end_date TYPE TIMESTAMP USING end_date;