CREATE TABLE users (
  username VARCHAR(50) NOT NULL,
  password VARCHAR(50) NOT NULL,
  session_token VARCHAR(36) UNIQUE,
  session_token_created DATETIME,
  session_token_last_used DATETIME,
  PRIMARY KEY (username)
) ENGINE = InnoDB;