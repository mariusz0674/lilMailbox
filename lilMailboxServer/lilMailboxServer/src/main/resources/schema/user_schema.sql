DROP TABLE IF EXISTS user_data;

CREATE TABLE IF NOT EXISTS user_data
(
    id uuid PRIMARY KEY,
    username VARCHAR(100) NOT NULL
);
