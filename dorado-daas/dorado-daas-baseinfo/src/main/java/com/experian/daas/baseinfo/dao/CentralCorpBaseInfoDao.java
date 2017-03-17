package com.experian.daas.baseinfo.dao;

import java.util.List;

import com.experian.dto.entity.baseinfo.CentralCorpBaseInfo;

public interface CentralCorpBaseInfoDao {
    int insert(CentralCorpBaseInfo record);

    List<CentralCorpBaseInfo> selectAll();

	List<CentralCorpBaseInfo> selectByCorpName(String corpName);
}