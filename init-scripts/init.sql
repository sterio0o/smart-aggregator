CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    keywords VARCHAR(255)[],
    delivery_method VARCHAR(255),
    report_frequency BIGINT
);

CREATE TABLE sources (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    source_url TEXT NOT NULL
);

CREATE TABLE user_subscription (
    user_id UUID NOT NULL,
    source_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (source_id) REFERENCES sources(id)
);

CREATE INDEX idx_email ON users(email);