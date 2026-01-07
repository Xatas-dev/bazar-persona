--liquibase formatted sql
--changeset AsterYng:1
--description user table
create table bazar_user(
    id uuid primary key,
    user_name varchar(128) not null,
    user_pic varchar(256),
    email varchar(128),
    first_name varchar(128),
    last_name varchar(128),
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now()
);

comment on column bazar_user.id IS 'User id from IDP';
comment on column bazar_user.user_name IS 'preferable_name from IDP';
comment on column bazar_user.user_pic IS 'User pic url from IDP / local S3 storage';
comment on column bazar_user.email IS 'User email from IDP';
comment on column bazar_user.first_name IS 'User first name from IDP';
comment on column bazar_user.last_name IS 'User last name from IDP';
comment on column bazar_user.created_at IS 'Created at time UTC';
comment on column bazar_user.updated_at IS 'Updated at time UTC';