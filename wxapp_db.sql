/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50554
Source Host           : localhost:3306
Source Database       : wxapp_cms

Target Server Type    : MYSQL
Target Server Version : 50554
File Encoding         : 65001

Date: 2018-10-24 23:33:51
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_buy_history
-- ----------------------------
DROP TABLE IF EXISTS `t_buy_history`;
CREATE TABLE `t_buy_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `custom` int(10) DEFAULT NULL,
  `buy_date` date DEFAULT NULL,
  `free` char(1) DEFAULT NULL COMMENT '0未赠送/1已赠送',
  PRIMARY KEY (`id`),
  KEY `FK_Reference_6` (`custom`),
  CONSTRAINT `FK_Reference_6` FOREIGN KEY (`custom`) REFERENCES `t_custom` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_custom
-- ----------------------------
DROP TABLE IF EXISTS `t_custom`;
CREATE TABLE `t_custom` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `user` varchar(30) DEFAULT NULL,
  `name` varchar(30) DEFAULT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL,
  `price` decimal(4,2) DEFAULT NULL,
  `remark` varchar(400) DEFAULT NULL,
  `create_date` date DEFAULT NULL,
  `deposit` char(1) DEFAULT NULL COMMENT '0未交/1已交/2已退',
  `goods` char(1) DEFAULT NULL COMMENT '0未押/1质押/2收回',
  PRIMARY KEY (`id`),
  KEY `FK_Reference_4` (`user`),
  CONSTRAINT `FK_Reference_4` FOREIGN KEY (`user`) REFERENCES `t_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_label
-- ----------------------------
DROP TABLE IF EXISTS `t_label`;
CREATE TABLE `t_label` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `custom` int(10) DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_Reference_7` (`custom`),
  CONSTRAINT `FK_Reference_7` FOREIGN KEY (`custom`) REFERENCES `t_custom` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_memo
-- ----------------------------
DROP TABLE IF EXISTS `t_memo`;
CREATE TABLE `t_memo` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `user` varchar(30) DEFAULT NULL,
  `content` text,
  `flag` char(1) DEFAULT NULL COMMENT '0否/1是',
  `type` char(1) DEFAULT NULL COMMENT '0单次/1周期',
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK_Reference_5` (`user`),
  CONSTRAINT `FK_Reference_5` FOREIGN KEY (`user`) REFERENCES `t_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_price_history
-- ----------------------------
DROP TABLE IF EXISTS `t_price_history`;
CREATE TABLE `t_price_history` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `custom` int(10) DEFAULT NULL,
  `price` decimal(4,2) DEFAULT NULL,
  `update_date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_Reference_8` (`custom`),
  CONSTRAINT `FK_Reference_8` FOREIGN KEY (`custom`) REFERENCES `t_custom` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_setting
-- ----------------------------
DROP TABLE IF EXISTS `t_setting`;
CREATE TABLE `t_setting` (
  `id` varchar(30) NOT NULL COMMENT '与用户编号一致',
  `list_size` int(4) DEFAULT NULL,
  `time` char(1) DEFAULT NULL COMMENT '0开启/1关闭',
  `free` int(4) DEFAULT NULL COMMENT '如买10送1',
  `gift` char(1) DEFAULT NULL COMMENT '0开启/1关闭',
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_Reference_3` FOREIGN KEY (`id`) REFERENCES `t_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_store
-- ----------------------------
DROP TABLE IF EXISTS `t_store`;
CREATE TABLE `t_store` (
  `id` varchar(30) NOT NULL COMMENT '与用户编号一致',
  `now_num` int(5) DEFAULT NULL,
  `warn_num` int(3) DEFAULT NULL,
  `warn_text` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_Reference_9` FOREIGN KEY (`id`) REFERENCES `t_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` varchar(30) NOT NULL,
  `name` varchar(30) DEFAULT NULL,
  `memo` text,
  `state` char(1) DEFAULT NULL COMMENT '0正常/1冻结',
  `last_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
