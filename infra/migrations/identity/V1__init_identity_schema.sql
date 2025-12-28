-- infra/migrations/identity/V1__init_identity_schema.sql
-- Create the identity schema if it doesn't exist. 
CREATE SCHEMA IF NOT EXISTS identity;

-- Set the search path so that subsequent tables are created in the new schema
SET search_path TO identity;

-- Create the users table
CREATE TABLE users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    username        VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- You may also want to add indexes for performance
CREATE INDEX idx_users_email ON users (email);