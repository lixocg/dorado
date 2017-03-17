/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : daas

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2017-01-16 16:41:59
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for litigation_case_category
-- ----------------------------
DROP TABLE IF EXISTS `litigation_case_category`;
CREATE TABLE `litigation_case_category` (
  `id` int(20) NOT NULL COMMENT '主键',
  `code` varchar(255) DEFAULT NULL COMMENT '案件类型代码',
  `name` varchar(255) DEFAULT NULL COMMENT '案件类型名称',
  `insert_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '数据入库时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
