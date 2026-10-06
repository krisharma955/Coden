CREATE TABLE bundles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(1000) NOT NULL,
    bundle_status VARCHAR(255) NOT NULL,
    bundle_category VARCHAR(255) NOT NULL,
    created_by_id UUID NOT NULL REFERENCES users(id),
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE bundle_files (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    bundle_id UUID NOT NULL REFERENCES bundles(id) ON DELETE CASCADE,
    file_name VARCHAR(255) NOT NULL,
    object_key VARCHAR(255) NOT NULL UNIQUE,
    file_path VARCHAR(1000) NOT NULL,
    size BIGINT NOT NULL,
    language VARCHAR(255) NOT NULL,
    extension VARCHAR(20) NOT NULL,
    uploaded_by_id UUID NOT NULL REFERENCES users(id),
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT uk_bundle_file_path UNIQUE (bundle_id, file_path)
);