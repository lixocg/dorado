package com.experian.daas.litigation.jobs;

import java.util.Date;

import org.apache.log4j.Logger;

import com.experian.daas.litigation.service.LitigationTransitService;

/**
 * DB2诉讼主表转化任务
 * @author e00898a
 *
 */
public class LitigationTransitJob implements Runnable {
	private Logger logger=Logger.getLogger(LitigationTransitJob.class);
	private LitigationTransitService service;
	private int curPage;
	private int pageSize;
	private Date updateDate;

	public LitigationTransitJob(LitigationTransitService service, int curPage,
			int pageSize,Date updateDate) {
		this.service=service;
		this.curPage=curPage;
		this.pageSize=pageSize;
		this.updateDate = updateDate;
	}

	@Override
	public void run() {
		logger.info("诉讼DB2主表线程"+Thread.currentThread().getName()+"启动！");
		try{
			service.transmitCorpLitigation(curPage, pageSize,updateDate);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("线程"+Thread.currentThread().getName()+"异常〉〉〉"+e.getMessage());
		}
	}

}