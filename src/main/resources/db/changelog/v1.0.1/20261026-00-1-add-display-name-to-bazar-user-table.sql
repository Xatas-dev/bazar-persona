--liquibase formatted sql
--changeset AsterYng:2
--description add display_name and username columns to bazar_user table

alter table bazar_user
add column display_name varchar(128) not null;

alter table bazar_user
    add column username varchar(128) not null;
