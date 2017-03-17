/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : daas

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2017-01-16 16:42:42
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for litigation_error_parsed_data_detail
-- ----------------------------
DROP TABLE IF EXISTS `litigation_error_parsed_data_detail`;
CREATE TABLE `litigation_error_parsed_data_detail` (
  `id` varchar(65) NOT NULL COMMENT '主键',
  `supplier_id` varchar(65) DEFAULT NULL COMMENT '供应商主键',
  `parsed_data_id` varchar(65) DEFAULT NULL COMMENT '案件编号',
  `supplier_parsed_data_id` varchar(65) DEFAULT NULL COMMENT ' 供应商案件编号，如optix的案件编号',
  `crawled_data_id` varchar(65) DEFAULT NULL COMMENT '抓取数据表主键id',
  `case_number` varchar(255) DEFAULT NULL COMMENT '案号',
  `simple_case_number` varchar(255) DEFAULT NULL COMMENT '简单案号，用于判重',
  `previous_case_number` varchar(255) DEFAULT NULL COMMENT '上期案号',
  `simple_previous_case_number` varchar(255) DEFAULT NULL COMMENT '简单上期案号',
  `if_be_court` char(1) DEFAULT NULL COMMENT '是否勾选（作报告用',
  `case_category_id` int(11) DEFAULT NULL,
  `accuse_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '案件审理日期',
  `court_id` int(11) DEFAULT NULL COMMENT ' 受理法院 / 执行法院',
  `case_title` text COMMENT '案件标题',
  `accuse_status` text COMMENT '案件状态',
  `accuser_list_no_anonymous` text,
  `appuser_list_no_anonymous` text,
  `party_list_no_anonymous` text,
  `simple_detail` text COMMENT '简单案由',
  `details` text COMMENT '详细案由',
  `total_amount_involved` decimal(20,2) DEFAULT NULL COMMENT '涉案金额',
  `trial_procedure` text,
  `win` varchar(255) DEFAULT NULL COMMENT ' 胜/败诉',
  `deduplicated` tinyint(1) DEFAULT NULL,
  `status` int(1) DEFAULT NULL COMMENT '（“0”表示该条数据解析成功；“1”，表示该条数据有用但未解析成功；“3”表示无用数据）',
  `reason` text,
  `created_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建日期',
  `updated_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `insert_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '数据入库时间',
  `expiry_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '判断是否有效用，若无效，则又有值，重复的和被替代的 数据 则该字段有值',
  `pk_id` bigint(20) DEFAULT NULL COMMENT '与诉讼平台数据关联用',
  `data_from` int(1) DEFAULT NULL COMMENT '数据来源,0-optix,1-db2主表,2-db2进程表,3-sqlserver诉讼平台',
  `error_type` int(1) DEFAULT NULL COMMENT '数据异常标示，0-判重被物理删除，1-数据不合格发生异常',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
