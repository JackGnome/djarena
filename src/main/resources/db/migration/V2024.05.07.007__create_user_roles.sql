CREATE TABLE user_roles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users (id),
    role_id UUID REFERENCES roles (id),
    UNIQUE(user_id, role_id)
);