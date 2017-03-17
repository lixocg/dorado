/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : daas

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2017-01-16 16:42:17
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for litigation_court
-- ----------------------------
DROP TABLE IF EXISTS `litigation_court`;
CREATE TABLE `litigation_court` (
  `id` int(20) NOT NULL COMMENT '主键',
  `district_code` varchar(255) DEFAULT NULL COMMENT '区域代码',
  `name` varchar(255) DEFAULT NULL COMMENT '区域名称',
  `court_code` varchar(255) DEFAULT NULL COMMENT '法院代码',
  `created_date` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `insert_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '数据入库时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
