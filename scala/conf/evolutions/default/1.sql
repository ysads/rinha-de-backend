# --- !Ups
CREATE TABLE IF NOT EXISTS `public`.`people` (
  `id` SERIAL PRIMARY KEY NOT NULL,
  `nickname` VARCHAR(32) NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `birthdate` VARCHAR(10) NOT NULL,
  `stack` VARCHAR(32) ARRAY NULL DEFAULT NULL
)
DEFAULT CHARACTER SET = utf8
 
# --- !Downs
drop table 'people'