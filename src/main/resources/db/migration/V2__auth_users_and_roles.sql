CREATE TABLE app_users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE user_roles (
    user_id UUID NOT NULL REFERENCES app_users (id) ON DELETE CASCADE,
    role VARCHAR(40) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    PRIMARY KEY (user_id, role),
    CHECK (role IN ('PLATFORM_ADMIN', 'LOCATION_ADMIN', 'PERFORMER', 'CLIENT'))
);

CREATE INDEX idx_user_roles_role ON user_roles (role);

ALTER TABLE performers
    ADD CONSTRAINT fk_performers_app_users
    FOREIGN KEY (app_user_id) REFERENCES app_users (id) ON DELETE CASCADE;

ALTER TABLE location_admins
    ADD CONSTRAINT fk_location_admins_app_users
    FOREIGN KEY (app_user_id) REFERENCES app_users (id) ON DELETE CASCADE;
