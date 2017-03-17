package com.experian.daas.litigation.service;

import java.util.Date;

/**
 * 旧数据转移到postgresql临时表
 * 
 * @author lixiongcheng
 *
 */
public interface LitigationTransitService {
	/**
	 * 获取未处理的诉讼主表记录总数
	 * 
	 * @return
	 */
	Integer getCorpLitigationCount(Date updateDate);

	/**
	 * 批量处理诉讼主表数据
	 * 
	 * @param curPage(当前页)
	 * @param pageSize(每页数据)
	 * @param logger(日志对象)
	 */
	void transmitCorpLitigation(int curPage, int pageSize, Date updateDate);

	/**
	 * 获取DB2进程表记录总数
	 * 
	 * @return
	 */
	Integer getLitigationHistroyCount(Date updateDate);

	/**
	 * 批量处理DB2进程表数据
	 * 
	 * @param curPage
	 * @param pageSize
	 */
	void transmitCorpLitigationHistory(int curPage, int pageSize,Date updateDate);

	/**
	 * 获取诉讼平台诉讼记录
	 * 
	 * @return
	 */
	Integer getSqlserverCorpLitigationCount(Date updateDate);

	/**
	 * 批量处理诉讼平台诉讼记录
	 * 
	 * @param curPage
	 * @param pageSize
	 */
	void transmitSqlserverCorpLitigation(int curPage, int pageSize,Date updateDate);

	/**
	 * 旧数据增量迁移
	 */
	void deltaOldDate();

}
