DROP TABLE messages;

CREATE TABLE IF NOT EXISTS messages
(
    id uuid PRIMARY KEY,
    from_user VARCHAR(100) NOT NULL,
    to_user VARCHAR(100) NOT NULL,
    s3_key VARCHAR(100) NOT NULL
);
