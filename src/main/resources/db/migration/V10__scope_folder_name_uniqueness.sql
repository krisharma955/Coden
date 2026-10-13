ALTER TABLE folders DROP CONSTRAINT IF EXISTS folders_name_key;

ALTER TABLE folders
    ADD CONSTRAINT uk_folders_user_name UNIQUE (user_id, name);
