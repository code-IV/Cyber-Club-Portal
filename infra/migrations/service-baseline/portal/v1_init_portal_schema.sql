
-- Create the portal schema if it doesn't exist.
CREATE SCHEMA IF NOT EXISTS portal;

-- Set the search path so that subsequent tables are created in the new schema
SET search_path TO portal;

CREATE TABLE user_setting (
    user_id UUID PRIMARY KEY,
    theme TEXT NOT NULL,
    notifications_enabled BOOLEAN NOT NULL,
    language_code TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP
);
