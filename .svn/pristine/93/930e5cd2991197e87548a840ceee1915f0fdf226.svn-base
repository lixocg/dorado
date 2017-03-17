/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : daas

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2017-01-16 16:43:00
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for litigation_party
-- ----------------------------
DROP TABLE IF EXISTS `litigation_party`;
CREATE TABLE `litigation_party` (
  `id` varchar(65) NOT NULL COMMENT '主键',
  `supplier_id` varchar(65) DEFAULT NULL COMMENT '供应商主键',
  `parsed_data_detail_id` varchar(65) DEFAULT NULL COMMENT 'litigation_parsed_data_detail 诉讼案件详情表主键',
  `name` varchar(255) DEFAULT NULL,
  `sbdnum` varchar(255) DEFAULT NULL,
  `party_category_id` int(11) DEFAULT NULL COMMENT '若企业/自然人为原告，则置值“1”；若为被告，则置值“2”，若为当事人，则置值“3”',
  `legal_representative` varchar(255) DEFAULT NULL COMMENT '法定代表人',
  `organization_code` varchar(255) DEFAULT NULL COMMENT '组织机构代码',
  `identification_number` varchar(255) DEFAULT NULL COMMENT '身份证号码--加密',
  `date_of_birth` varchar(255) DEFAULT NULL COMMENT '出生日期--加密',
  `address` varchar(2048) DEFAULT NULL COMMENT '地址',
  `organization_name` varchar(255) DEFAULT NULL COMMENT '单位名称',
  `amount_involved` decimal(20,2) DEFAULT NULL COMMENT '履行金额',
  `amount_to_be_executed` decimal(20,2) DEFAULT NULL COMMENT '未执行金额',
  `updated_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `accuse_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '案件审理日期',
  `insert_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '数据入库时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
