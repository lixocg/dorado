/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : daas

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2017-01-16 16:43:11
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for litigation_source_url
-- ----------------------------
DROP TABLE IF EXISTS `litigation_source_url`;
CREATE TABLE `litigation_source_url` (
  `id` int(20) NOT NULL COMMENT ' 主键',
  `source_url` text COMMENT '来源url',
  `description` text COMMENT '诉讼数据源描述',
  `created_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'optix创建时间',
  `insert_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '数据入库时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
