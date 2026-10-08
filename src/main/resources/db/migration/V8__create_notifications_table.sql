CREATE TABLE notifications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE, -- person receiving notification
    notification_type VARCHAR(255) NOT NULL,
    message VARCHAR(500) NOT NULL,
    reference_id UUID NOT NULL,
    actor_id UUID NOT NULL REFERENCES users(id), -- person triggering notification
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);