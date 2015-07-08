--CREATE DATABASE IF NOT EXISTS pv_db$$
--use pv_db$$
--ALTER DATABASE pv_db CHARACTER SET utf8 COLLATE utf8_unicode_ci$$
DROP TABLE IF EXISTS synonyms$$
DROP TABLE IF EXISTS words$$
DROP TABLE IF EXISTS users$$
CREATE TABLE IF NOT EXISTS users (
  user_id INTEGER NOT NULL AUTO_INCREMENT,
  user_email VARCHAR(50) NOT NULL ,
  password VARCHAR(60) NOT NULL ,
  enabled TINYINT NOT NULL DEFAULT 1 ,
  PRIMARY KEY (user_id)
) ENGINE=InnoDB$$

CREATE TABLE IF NOT EXISTS words (
  word_id INTEGER NOT NULL AUTO_INCREMENT,
  user_id INTEGER NOT NULL,
  word VARCHAR(255) NOT NULL,
  word_translation VARCHAR(255),
  word_description VARCHAR(255),
  examples TINYTEXT,  
  PRIMARY KEY(word_id),
  FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB$$

CREATE TABLE IF NOT EXISTS synonyms (
  word_id INTEGER NOT NULL,
  synonym_word_id INTEGER NOT NULL,
  FOREIGN KEY(word_id) REFERENCES words(word_id)  ON DELETE CASCADE,
  FOREIGN KEY(synonym_word_id) REFERENCES words(word_id) ON DELETE CASCADE
) ENGINE=InnoDB$$

DROP PROCEDURE IF EXISTS add_synonym$$

CREATE DEFINER = 'pervoc'@'localhost' PROCEDURE add_synonym (word_id INT, synonym_word_id INT)
SQL SECURITY INVOKER
BEGIN  
	INSERT IGNORE INTO synonyms VALUES(word_id, synonym_word_id);
END $$

DROP TRIGGER IF EXISTS word_updater$$

CREATE TRIGGER word_updater BEFORE UPDATE ON words
FOR EACH ROW
BEGIN
DELETE FROM synonyms WHERE word_id = OLD.word_id OR synonym_word_id = OLD.word_id;
END $$

DELETE FROM users$$
