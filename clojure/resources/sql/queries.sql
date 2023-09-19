-- :name create-person! :! :1
INSERT INTO people (nickname, name, birthdate, stack)
VALUES (:nickname, :name, :birthdate, :stack)
RETURNING id

-- :name get-person-by-id :? :1
SELECT * FROM people
WHERE id::text = :id

-- :name get-all-people :? :*
SELECT * FROM people

-- :name count-all-people :! :1
SELECT count(*) FROM people

-- :name search-people :? :*
SELECT * FROM people
WHERE search ILIKE :term

-- :name explain-search-people :? :*
EXPLAIN ANALYZE
SELECT * FROM people
WHERE search ILIKE :term