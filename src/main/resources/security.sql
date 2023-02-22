CREATE DATABASE if not exists security;
use security;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE if not  exists `user` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `avatar` varchar (255),
  `des` varchar(255),
  `tel` varchar(255),
  `email` varchar(255),
  `create_time` timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_time` timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
INSERT INTO `user` VALUES (2, 'editor', '$2a$10$JJ82/k3LicFc6kbCYP7ekO4MyepyQCL7w4vX1cgPzgy91nC2sALhi','testavatar','admin','admin','admin',NOW(),NOW());
INSERT INTO `user` VALUES (1, 'admin', '$2a$10$JJ82/k3LicFc6kbCYP7ekO4MyepyQCL7w4vX1cgPzgy91nC2sALhi','testavatar','editor','admin','admin',NOW(),NOW());

CREATE TABLE if not  exists `role` (
                        `id` bigint(11) NOT NULL AUTO_INCREMENT,
                        `name` varchar(255) NOT NULL,
                        `code` varchar(255) NOT NULL,
                        `des` varchar(255),
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
INSERT INTO `role` VALUES (2, 'editor','test','editor');
INSERT INTO `role` VALUES (1, 'admin','test','admin');

CREATE TABLE if not  exists `role_user` (
  `user_id` bigint(11) NOT NULL,
  `role_id` bigint(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
TRUNCATE TABLE role_user;
INSERT INTO `role_user` VALUES (1, 1);
INSERT INTO `role_user` VALUES (1, 2);
INSERT INTO `role_user` VALUES (2, 2);

CREATE TABLE if not  exists `permission` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `pid` bigint(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
INSERT INTO `permission` VALUES (1, 'user/**', '用户列表', NULL, 0);
INSERT INTO `permission` VALUES (2, '/admin/**', '获取用户信息', NULL, 0);

CREATE TABLE if not  exists `account_state` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `userid` varchar(64) NOT NULL,
  `account_non_expired` int NOT NULL,
  `account_non_locked` int NOT NULL,
  `credentials_non_expired` int NOT NULL,
  `enabled` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO `account_state` VALUES (1,'1',1,1,1,1);

CREATE TABLE if not  exists `refresh_token` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `usename` varchar(255) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
