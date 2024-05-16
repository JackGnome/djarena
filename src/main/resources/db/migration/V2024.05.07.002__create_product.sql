CREATE TABLE product (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    vendor_code VARCHAR(10) UNIQUE,
    name VARCHAR(255) NOT NULL,
    price NUMERIC(10, 4),
    preview VARCHAR(1024) NOT NULL,
    brand_id UUID REFERENCES brand (id)
);