CREATE TABLE snippet_folder_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    folder_id UUID NOT NULL REFERENCES folders(id),
    snippet_id UUID NOT NULL REFERENCES snippets(id),
    added_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT uk_folder_snippet UNIQUE (folder_id, snippet_id)
);