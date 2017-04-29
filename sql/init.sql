CREATE TABLE users (
  username VARCHAR(50) NOT NULL,
  password VARCHAR(60) NOT NULL,
  sessionToken VARCHAR(36) UNIQUE,
  sessionTokenCreated DATETIME,
  sessionTokenLastUsed DATETIME,
  PRIMARY KEY (username)
) ENGINE = InnoDB;