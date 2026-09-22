SET default_tablespace = gastronomiespace;

-- text statt varchar(n):
-- "There is no performance difference among these three types, apart from a few extra CPU cycles
-- to check the length when storing into a length-constrained column"

-- https://www.postgresql.org/docs/current/sql-createtable.html
-- https://www.postgresql.org/docs/current/datatype.html
-- https://www.postgresql.org/docs/current/functions-uuid.html
CREATE TABLE IF NOT EXISTS adresse (
                                       id        UUID PRIMARY KEY DEFAULT uuidv7(),
    -- https://www.postgresql.org/docs/current/ddl-constraints.html#DDL-CONSTRAINTS-CHECK-CONSTRAINTS
    strasse   TEXT NOT NULL,
    plz       TEXT NOT NULL CHECK (plz ~ '\d{5}'),
    ort       TEXT NOT NULL
    );

CREATE TABLE IF NOT EXISTS restaurant (
                                          id            UUID PRIMARY KEY DEFAULT uuidv7(),
    -- https://www.postgresql.org/docs/current/datatype-numeric.html#DATATYPE-INT
    version       INTEGER NOT NULL DEFAULT 0,
    -- impliziter Index als B-Baum durch UNIQUE
    name          TEXT NOT NULL UNIQUE,
    -- https://www.postgresql.org/docs/current/ddl-constraints.html#DDL-CONSTRAINTS-FK
    adresse_id    UUID NOT NULL UNIQUE REFERENCES adresse,
    -- https://www.postgresql.org/docs/current/datatype-datetime.html
    erzeugt       TIMESTAMP NOT NULL,
    aktualisiert  TIMESTAMP NOT NULL
    );

CREATE TABLE IF NOT EXISTS speise (
                                      id            UUID PRIMARY KEY DEFAULT uuidv7(),
    -- https://www.postgresql.org/docs/current/datatype-numeric.html#DATATYPE-NUMERIC-DECIMAL
    -- 5 Stellen insgesamt, davon 2 Nachkommastellen (z.B. 999.99)
    name          TEXT NOT NULL,
    preis         NUMERIC(5,2) NOT NULL CHECK (preis >= 0.00),
    -- nicht NOT NULL wegen gerichteter Beziehung und UPDATE durch Hibernate
    restaurant_id UUID REFERENCES restaurant,
    idx           INTEGER NOT NULL DEFAULT 0
    );
