package com.experian.daas.litigation.dao;

import java.util.List;

import com.experian.dto.entity.litigation.LitigationCrawledData;

public interface LitigationCrawledDataDao {
    int deleteByPrimaryKey(String id);

    int insert(LitigationCrawledData record);

    LitigationCrawledData selectByPrimaryKey(String id);

    List<LitigationCrawledData> selectAll();

    int updateByPrimaryKey(LitigationCrawledData record);
}