package com.experian.daas.baseinfo.dao;

import java.util.List;

import com.experian.dto.entity.baseinfo.CreditCorpBaseInfo;

public interface CreditCorpBaseInfoDao {
    int insert(CreditCorpBaseInfo record);

    List<CreditCorpBaseInfo> selectAll();

	List<CreditCorpBaseInfo> selectByCorpName(String corpName);
}