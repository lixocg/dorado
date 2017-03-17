package com.experian.daas.litigation.service;

import com.experian.dto.entity.litigation.LitigationTransmissionLog;

public interface LitigationTransmissionLogService {

	/**
	 * 初始化迁移log表
	 * 
	 * @return
	 */
	LitigationTransmissionLog initialize();

	/**
	 * 获取最新迁移log记录
	 * 
	 * @return
	 */
	LitigationTransmissionLog getLastestLog();

}
