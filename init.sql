CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    keywords TEXT,
    delivery_method VARCHAR(255) NOT NULL,
    report_frequency INTERVAL NOT NULL
);

CREATE TABLE sources (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    source_url TEXT NOT NULL
);

CREATE TABLE user_subscription (
    user_id UUID NOT NULL,
    source_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (source_id) REFERENCES sources(id)
);