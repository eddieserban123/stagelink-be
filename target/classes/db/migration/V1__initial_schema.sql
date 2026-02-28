CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TYPE booking_request_status AS ENUM ('PENDING', 'APPROVED', 'REJECTED', 'WITHDRAWN');
CREATE TYPE slot_status AS ENUM ('OPEN', 'LOCKED', 'CANCELLED');
CREATE TYPE notification_status AS ENUM ('PENDING', 'SENT', 'FAILED');
CREATE TYPE notification_type AS ENUM ('BOOKING_REQUEST_CREATED', 'BOOKING_REQUEST_UPDATED');

CREATE TABLE performers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    app_user_id UUID UNIQUE,
    title VARCHAR(100),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    number_of_members INTEGER NOT NULL DEFAULT 1 CHECK (number_of_members > 0),
    special_requirements TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE performer_phone_numbers (
    id BIGSERIAL PRIMARY KEY,
    performer_id UUID NOT NULL REFERENCES performers (id) ON DELETE CASCADE,
    phone_number VARCHAR(40) NOT NULL,
    is_primary BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (performer_id, phone_number)
);

CREATE TABLE locations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    address TEXT NOT NULL,
    google_maps_link TEXT,
    latitude NUMERIC(9, 6),
    longitude NUMERIC(9, 6),
    description TEXT,
    timezone VARCHAR(80) NOT NULL DEFAULT 'UTC',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CHECK (latitude IS NULL OR (latitude BETWEEN -90 AND 90)),
    CHECK (longitude IS NULL OR (longitude BETWEEN -180 AND 180))
);

CREATE TABLE location_phone_numbers (
    id BIGSERIAL PRIMARY KEY,
    location_id UUID NOT NULL REFERENCES locations (id) ON DELETE CASCADE,
    phone_number VARCHAR(40) NOT NULL,
    is_primary BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (location_id, phone_number)
);

CREATE TABLE location_admins (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    app_user_id UUID UNIQUE,
    location_id UUID NOT NULL REFERENCES locations (id) ON DELETE CASCADE,
    full_name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(40),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE location_slots (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    location_id UUID NOT NULL REFERENCES locations (id) ON DELETE CASCADE,
    starts_at TIMESTAMPTZ NOT NULL,
    ends_at TIMESTAMPTZ NOT NULL,
    status slot_status NOT NULL DEFAULT 'OPEN',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CHECK (ends_at > starts_at),
    UNIQUE (location_id, starts_at, ends_at)
);

CREATE TABLE booking_requests (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    slot_id UUID NOT NULL REFERENCES location_slots (id) ON DELETE CASCADE,
    performer_id UUID NOT NULL REFERENCES performers (id) ON DELETE CASCADE,
    request_message TEXT,
    status booking_request_status NOT NULL DEFAULT 'PENDING',
    requested_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    reviewed_at TIMESTAMPTZ,
    reviewed_by_admin_id UUID REFERENCES location_admins (id),
    rejection_reason TEXT,
    UNIQUE (slot_id, performer_id)
);

CREATE UNIQUE INDEX uq_booking_requests_one_approved_per_slot
    ON booking_requests (slot_id)
    WHERE status = 'APPROVED';

CREATE TABLE public_events (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    slot_id UUID NOT NULL UNIQUE REFERENCES location_slots (id) ON DELETE CASCADE,
    booking_request_id UUID NOT NULL UNIQUE REFERENCES booking_requests (id) ON DELETE CASCADE,
    location_id UUID NOT NULL REFERENCES locations (id) ON DELETE CASCADE,
    performer_id UUID NOT NULL REFERENCES performers (id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    published_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE admin_notifications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    admin_id UUID NOT NULL REFERENCES location_admins (id) ON DELETE CASCADE,
    booking_request_id UUID NOT NULL REFERENCES booking_requests (id) ON DELETE CASCADE,
    type notification_type NOT NULL,
    status notification_status NOT NULL DEFAULT 'PENDING',
    payload JSONB,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    sent_at TIMESTAMPTZ
);

CREATE INDEX idx_performer_phone_numbers_performer_id ON performer_phone_numbers (performer_id);
CREATE INDEX idx_location_phone_numbers_location_id ON location_phone_numbers (location_id);
CREATE INDEX idx_location_admins_location_id ON location_admins (location_id);
CREATE INDEX idx_location_slots_location_id_starts_at ON location_slots (location_id, starts_at);
CREATE INDEX idx_booking_requests_slot_id_status ON booking_requests (slot_id, status);
CREATE INDEX idx_booking_requests_performer_id ON booking_requests (performer_id);
CREATE INDEX idx_public_events_location_id ON public_events (location_id);
CREATE INDEX idx_public_events_performer_id ON public_events (performer_id);
CREATE INDEX idx_admin_notifications_admin_id_status ON admin_notifications (admin_id, status);
