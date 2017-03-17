package com.experian.daas.litigation.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.experian.dto.entity.litigation.LitigationErrorParsedDataDetail;

public interface LitigationErrorParsedDataDetailDao {
    int deleteByPrimaryKey(String id);

    int insert(LitigationErrorParsedDataDetail record);

    LitigationErrorParsedDataDetail selectByPrimaryKey(String id);

    List<LitigationErrorParsedDataDetail> selectAll();

    int updateByPrimaryKey(LitigationErrorParsedDataDetail record);

	int getErrorDetailCount(@Param("errorType")int errorType);

	int getErrorCrawledCount(@Param("errorType")int errorType);

	int getErrorPartyCount(@Param("errorType")int errorType);
}