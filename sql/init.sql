CREATE SCHEMA `las3014`;

DROP TABLE IF EXISTS `user_topic_mapping`;
DROP TABLE IF EXISTS `user_digest_mapping`;
DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `topics`;
DROP TABLE IF EXISTS `stories`;
DROP TABLE IF EXISTS `digests`;
DROP TABLE IF EXISTS `BATCH_STEP_EXECUTION_CONTEXT`;
DROP TABLE IF EXISTS `BATCH_JOB_EXECUTION_CONTEXT`;
DROP TABLE IF EXISTS `BATCH_STEP_EXECUTION`;
DROP TABLE IF EXISTS `BATCH_JOB_EXECUTION_PARAMS`;
DROP TABLE IF EXISTS `BATCH_JOB_EXECUTION`;
DROP TABLE IF EXISTS `BATCH_JOB_INSTANCE`;
DROP TABLE IF EXISTS `BATCH_STEP_EXECUTION_SEQ`;
DROP TABLE IF EXISTS `BATCH_JOB_EXECUTION_SEQ`;
DROP TABLE IF EXISTS `BATCH_JOB_SEQ`;

CREATE TABLE `users` (
  `user_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL,
  `password` VARCHAR(60) NOT NULL,
  `session_token` VARCHAR(36) UNIQUE,
  `session_token_created` DATETIME,
  `session_token_last_used` DATETIME,
  PRIMARY KEY (`user_id`)
) ENGINE = InnoDB;

CREATE TABLE `stories` (
  `story_id` BIGINT UNSIGNED NOT NULL,
  `score` int NOT NULL,
  `title` varchar(500) NOT NULL,
  `url` varchar(500),
  `date_created` DATETIME,
  `deleted` bit(1) DEFAULT 0,
  PRIMARY KEY (`story_id`)
) ENGINE = InnoDB;

CREATE TABLE `topics` (
  `topic_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `topic_name` VARCHAR(50) NOT NULL,
  `top_story_id` BIGINT UNSIGNED,
  PRIMARY KEY (`topic_id`),
  CONSTRAINT `fk_topics_topstoryid` FOREIGN KEY (`top_story_id`) REFERENCES `stories` (`story_id`) ON DELETE CASCADE ON UPDATE CASCADE
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

CREATE TABLE `digests` (
  `digest_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `day_of_week` DATE,
  `topic_id` BIGINT UNSIGNED,
  `story_id` BIGINT UNSIGNED,
  PRIMARY KEY (`digest`),
  CONSTRAINT `fk_digests_topicid` FOREIGN KEY (`topic_id`) REFERENCES `topics` (`topic_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_digests_storyid` FOREIGN KEY (`story_id`) REFERENCES `stories` (`story_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB;

CREATE TABLE `user_digest_mapping` (
  `digest_id` BIGINT UNSIGNED NOT NULL,
  `user_id` BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (`digest_id` , `user_id`),
  KEY `fk_userdigestmapping_user_idx` (`user_id`),
  CONSTRAINT `fk_userdigestmapping_digestid` FOREIGN KEY (`digest_id`) REFERENCES `digests` (`digest_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_userdigestmapping_userid` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB;