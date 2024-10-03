-- File: create_tables.sql

create database tvsm_alexa_oauth;

use tvsm_alexa_oauth;

-- Table: mobile_orgid_vehicleid
CREATE TABLE IF NOT EXISTS tvsm_alexa_oauth.mobile_orgid_vehicleid (
    mobile varchar(255) not null,
    created varchar(255),
    modified varchar(255),
    orgid varchar(255),
    subscription_end_date datetime,
    subscription_status integer,
    vehicleid varchar(255),
    primary key (mobile)
);

-- Table: oauth_access_token
CREATE TABLE IF NOT EXISTS tvsm_alexa_oauth.oauth_access_token (
    user_name varchar(255) not null,
    authentication longblob,
    authentication_id varchar(255),
    client_id varchar(255),
    refresh_token varchar(255),
    token longblob,
    token_id varchar(255),
    primary key (user_name)
);

-- Table: tvsm_otp
CREATE TABLE IF NOT EXISTS tvsm_alexa_oauth.tvsm_otp (
    id integer not null auto_increment,
    created DATETIME,
    expire DATETIME,
    mobile_number varchar(255),
    modified DATETIME,
    num_sent integer,
    otp varchar(255),
    used integer,
    primary key (id)
);

-- Table: oauth_refresh_token
CREATE TABLE IF NOT EXISTS tvsm_alexa_oauth.oauth_refresh_token (
    token_id varchar(255) NOT NULL,
    token blob NOT NULL,
    authentication blob NOT NULL
);