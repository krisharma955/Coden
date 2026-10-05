CREATE TABLE snippet_stars (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id),
    snippet_id UUID NOT NULL REFERENCES snippets(id),
    starred_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT uk_snippet_star_user UNIQUE (user_id, snippet_id)
);

