package com.experian.daas.baseinfo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.experian.dto.entity.baseinfo.CreditCorpBaseInfo;
import com.experian.dto.entity.baseinfo.CreditReportOrder;

public interface CreditReportOrderDao {
    int deleteByPrimaryKey(String ordernum);

    int insert(CreditReportOrder record);

    CreditReportOrder selectByPrimaryKey(String ordernum);

    List<CreditReportOrder> selectAll();

    int updateByPrimaryKey(CreditReportOrder record);

	List<CreditReportOrder> selectByCorpBaseInfoSbdnums(@Param("creditCorpBaseInfos")List<CreditCorpBaseInfo> creditCorpBaseInfos);
}