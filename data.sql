-- Database: porteiro

DROP DATABASE IF EXISTS porteiro;

CREATE DATABASE porteiro
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Portuguese_Brazil.1252'
    LC_CTYPE = 'Portuguese_Brazil.1252'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

INSERT INTO roles values (1, 'ADM'), (2, 'RES'), (3, 'CON'), (4, 'MOD')