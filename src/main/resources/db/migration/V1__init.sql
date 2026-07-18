CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(80) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL
);

CREATE TABLE short_links(
    id BIGSERIAL PRIMARY KEY,
    short_code VARCHAR(255) NOT NULL UNIQUE ,
    original_link VARCHAR(255) NOT NULL,
    user_id BIGINT REFERENCES users(id) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    is_active BOOLEAN DEFAULT TRUE NOT NULL
);

CREATE TABLE click_events(
    id BIGSERIAL PRIMARY KEY,
    short_link_id BIGINT REFERENCES short_links(id) NOT NULL ,
    clicked_at TIMESTAMP NOT NULL,
    ip_address VARCHAR(255) NOT NULL,
    referrer VARCHAR(255)
);