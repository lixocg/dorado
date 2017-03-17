package com.experian.daas.litigation.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.experian.daas.litigation.dao.LitigationTransmissionLogDao;
import com.experian.daas.litigation.service.LitigationTransmissionLogService;
import com.experian.dto.entity.litigation.LitigationTransmissionLog;

@Service
public class LitigationTransmissionLogServiceImpl implements LitigationTransmissionLogService {
	@Resource
	private LitigationTransmissionLogDao transmissionLogDao;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public LitigationTransmissionLog initialize() {
		LitigationTransmissionLog record = new LitigationTransmissionLog();
		Date updateDate = new Date();
		record.setUpdatedate(updateDate);
		record.setBegindate(new Date());
		transmissionLogDao.insert(record);
		return record;
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public LitigationTransmissionLog getLastestLog(){
		LitigationTransmissionLog lastestLog = transmissionLogDao.selectLastestLog();
		return lastestLog;
	}
}
