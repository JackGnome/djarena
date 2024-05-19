CREATE TABLE brand (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) UNIQUE NOT NULL,
    created_at TIMESTAMP,
    created_by UUID,
    updated_at TIMESTAMP,
    updated_by UUID,
    archived BOOLEAN NOT NULL DEFAULT FALSE
);