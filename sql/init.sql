CREATE SCHEMA `las3014`;

CREATE TABLE users (
  user_id int NOT NULL AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL,
  password VARCHAR(60) NOT NULL,
  session_token VARCHAR(36) UNIQUE,
  session_token_created DATETIME,
  session_token_last_used DATETIME,
  PRIMARY KEY (user_id)
) ENGINE = InnoDB;

CREATE TABLE topics (
  topic_id int NOT NULL AUTO_INCREMENT,
  topic_name VARCHAR(50) NOT NULL,
  PRIMARY KEY (topic_id)
) ENGINE = InnoDB;

CREATE TABLE user_topic_mapping (
  user_id int NOT NULL,
  topic_id int NOT NULL
) ENGINE = InnoDB;