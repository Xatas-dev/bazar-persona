--liquibase formatted sql
--changeset AsterYng:1
--description remove redundant columns from bazar_user table

ALTER TABLE bazar_user DROP COLUMN IF EXISTS user_pic;
ALTER TABLE bazar_user DROP COLUMN IF EXISTS email;
ALTER TABLE bazar_user DROP COLUMN IF EXISTS first_name;
ALTER TABLE bazar_user DROP COLUMN IF EXISTS last_name;