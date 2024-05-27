CREATE TABLE auth_tokens (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    value VARCHAR(300) NOT NULL,
    type token_type NOT NULL,
    created_at TIMESTAMP,
    expired_at TIMESTAMP,
    revoked BOOLEAN NOT NULL DEFAULT FALSE,
    user_id UUID REFERENCES users (id),
    device_id VARCHAR(50) NOT NULL
);