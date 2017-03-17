package com.experian.daas.litigation.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.experian.core.constant.LitiBusiConstant;
import com.experian.daas.litigation.dao.LitigationErrorCrawledDataDao;
import com.experian.daas.litigation.dao.LitigationErrorParsedDataDetailDao;
import com.experian.daas.litigation.dao.LitigationErrorPartyDao;
import com.experian.daas.litigation.service.LitigationErrorService;

@Service("litigationErrorService")
public class LitigationErrorServiceImpl implements LitigationErrorService {
	@Resource
	private LitigationErrorParsedDataDetailDao errorParsedDataDetailDao;

	@Resource
	private LitigationErrorCrawledDataDao errorCrawledDataDao;

	@Resource
	private LitigationErrorPartyDao errorPartyDao;

	@Override
	public ErrorStatistic getErrorStatistic(int errorType) {
		ErrorStatistic es = new ErrorStatistic();
		int errorDetailCount = 0;
		int errorCrawledCount = 0;
		int errorPartyCount = 0;
		if (LitiBusiConstant.ErrorType.contains(errorType)) {
			errorDetailCount = errorParsedDataDetailDao.getErrorDetailCount(errorType);
			errorCrawledCount = errorParsedDataDetailDao.getErrorCrawledCount(errorType);
			errorPartyCount = errorParsedDataDetailDao.getErrorPartyCount(errorType);
		}
		es.setErrorCrawledDataNum(errorCrawledCount);
		es.setErrorDetailNum(errorDetailCount);
		es.setErrorPartyNum(errorPartyCount);
		return es;
	}

}
