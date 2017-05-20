CREATE SCHEMA `las3014`;

DROP TABLE IF EXISTS `user_topic_mapping`;
DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `topics`;
DROP TABLE IF EXISTS `stories`;

CREATE TABLE `users` (
  `user_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL,
  `password` VARCHAR(60) NOT NULL,
  `session_token` VARCHAR(36) UNIQUE,
  `session_token_created` DATETIME,
  `session_token_last_used` DATETIME,
  PRIMARY KEY (`user_id`)
) ENGINE = InnoDB;

CREATE TABLE `topics` (
  `topic_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `topic_name` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`topic_id`)
) ENGINE = InnoDB;

CREATE TABLE `user_topic_mapping` (
  `mapping_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT UNSIGNED NOT NULL,
  `topic_id` BIGINT UNSIGNED NOT NULL,
  `is_enabled` bit(1) DEFAULT 1,
  `interested_from` DATETIME,
  `interested_to` DATETIME,
  PRIMARY KEY (`mapping_id`),
  CONSTRAINT `fk_usertopicmapping_userid` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_usertopicmapping_topicid` FOREIGN KEY (`topic_id`) REFERENCES `topics` (`topic_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB;

CREATE TABLE `stories` (
  `story_id` BIGINT UNSIGNED NOT NULL ,
  `score` int NOT NULL,
  `title` varchar(500) NOT NULL,
  `url` varchar(500),
  `date_created` DATETIME,
  PRIMARY KEY (`story_id`)
) ENGINE = InnoDB;