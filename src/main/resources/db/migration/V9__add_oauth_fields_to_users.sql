ALTER TABLE users
    ADD COLUMN github_id   BIGINT UNIQUE,
    ADD COLUMN avatar_url  VARCHAR(255),
    ADD COLUMN profile_url VARCHAR(255);