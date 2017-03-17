package com.experian.daas.baseinfo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.experian.dto.entity.baseinfo.CentralCorpBaseInfo;
import com.experian.dto.entity.baseinfo.CentralReportOrder;

public interface CentralReportOrderDao {
	int deleteByPrimaryKey(String ordernum);

	int insert(CentralReportOrder record);

	CentralReportOrder selectByPrimaryKey(String ordernum);

	List<CentralReportOrder> selectAll();

	int updateByPrimaryKey(CentralReportOrder record);

	/**
	 * 中央库查询订单
	 * 
	 * @param centralCorpBaseInfos
	 * @return
	 */
	List<CentralReportOrder> selectByCorpBaseInfoSbdnums(
			@Param("centralCorpBaseInfos") List<CentralCorpBaseInfo> centralCorpBaseInfos);

}