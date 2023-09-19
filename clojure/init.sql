CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- Needed because the default array_to_string is by design generic (uses anyarray type)
-- and thus potentially prone to side-effects.
-- Since this wrapper only accepts text[], it's safe an can be used with generated columns.
CREATE OR REPLACE FUNCTION immutable_arr_to_text(text[]) 
  RETURNS text LANGUAGE sql IMMUTABLE AS $$SELECT array_to_string($1, ' ')$$;

CREATE TABLE IF NOT EXISTS people (
  id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
  nickname VARCHAR(32) UNIQUE NOT NULL,
  name VARCHAR(100) NOT NULL,
  birthdate VARCHAR(10) NOT NULL,
  stack TEXT[],
  search TEXT GENERATED ALWAYS AS (
    CASE
      WHEN stack IS NULL THEN name || ' ' || nickname
      ELSE name || ' ' || nickname || ' ' || immutable_arr_to_text(stack)
    END
  ) STORED
);

CREATE INDEX people_searchable_idx ON people USING GIN (search gin_trgm_ops);