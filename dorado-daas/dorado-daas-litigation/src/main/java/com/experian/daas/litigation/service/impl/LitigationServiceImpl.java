package com.experian.daas.litigation.service.impl;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.experian.daas.litigation.dao.LitigationCaseCategoryDao;
import com.experian.daas.litigation.dao.LitigationTempParsedDataDetailDao;
import com.experian.daas.litigation.dao.LitigationTempPartyDao;
import com.experian.daas.litigation.service.LitigationService;
import com.experian.dto.entity.litigation.LitigationCaseCategory;

@Service("litigationService")
public class LitigationServiceImpl implements LitigationService {

	@Resource
	private LitigationCaseCategoryDao litigationCaseCategoryDao;
	
	@Resource
	private LitigationTempParsedDataDetailDao tempParsedDataDetailDao;
	
	@Resource
	private LitigationTempPartyDao tempPartyDao;

	@Override
	public LitigationCaseCategory getLitigationCaseCategory(@Param("id")Integer id) {
		return litigationCaseCategoryDao.selectByPrimaryKey(id);
	}

}
