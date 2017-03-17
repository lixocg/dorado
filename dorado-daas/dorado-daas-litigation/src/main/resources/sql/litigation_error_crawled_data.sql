/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : daas

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2017-01-16 16:42:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for litigation_error_crawled_data
-- ----------------------------
DROP TABLE IF EXISTS `litigation_error_crawled_data`;
CREATE TABLE `litigation_error_crawled_data` (
  `id` varchar(65) NOT NULL COMMENT '主键',
  `supplier_id` varchar(65) DEFAULT NULL,
  `source_url_id` int(20) DEFAULT NULL COMMENT '来源网址id',
  `content_url` text COMMENT '抓取数据内容页url',
  `data`  mediumtext COMMENT '抓取的诉讼数据' ,
  `title` text COMMENT '标题',
  `publish_date` datetime DEFAULT NULL COMMENT '发布日期',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `insert_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '数据入库时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
