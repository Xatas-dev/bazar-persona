--liquibase formatted sql
--changeset AsterYng:1
--description remove redundant columns from bazar_user table

alter table bazar_user
drop column user_pic;

alter table bazar_user
drop column user_name;

alter table bazar_user
drop column email;

alter table bazar_user
drop column first_name;

alter table bazar_user
drop column last_name;