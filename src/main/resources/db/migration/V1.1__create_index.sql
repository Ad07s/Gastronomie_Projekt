-- https://www.postgresql.org/docs/current/manage-ag-tablespaces.html
SET default_tablespace = gastronomiespace;

-- Indexe mit pgAdmin auflisten: "Query Tool" verwenden mit
--  SELECT   tablename, indexname, indexdef, tablespace
--  FROM     pg_indexes
--  WHERE    schemaname = 'gastronomie'
--  ORDER BY tablename, indexname;

-- default: btree
-- https://www.postgresql.org/docs/current/sql-createindex.html

CREATE INDEX IF NOT EXISTS adresse_plz_idx ON adresse(plz);
CREATE INDEX IF NOT EXISTS speise_name_idx ON speise(name);
CREATE INDEX IF NOT EXISTS speise_restaurant_id_idx ON speise(restaurant_id);
