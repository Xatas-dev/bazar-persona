--liquibase formatted sql
--changeset AsterYng:4
--description create user_avatar table

CREATE TABLE user_avatar
(
    id             BIGSERIAL PRIMARY KEY,
    user_id        UUID        NOT NULL REFERENCES bazar_user (id),
    file_id        UUID        NOT NULL,
    is_active     BOOL        NOT NULL,
    status         VARCHAR(16) NOT NULL,
    url            TEXT,
    error_reason   TEXT,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at     TIMESTAMPTZ NOT NULL DEFAULT now(),

    CONSTRAINT uq_avatar_file_id UNIQUE (file_id)
);