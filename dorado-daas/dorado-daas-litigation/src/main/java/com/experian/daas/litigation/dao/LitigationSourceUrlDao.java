package com.experian.daas.litigation.dao;

import java.util.List;

import com.experian.dto.entity.litigation.LitigationSourceUrl;

public interface LitigationSourceUrlDao {
    int deleteByPrimaryKey(String id);

    int insert(LitigationSourceUrl record);

    LitigationSourceUrl selectByPrimaryKey(Integer id);

    List<LitigationSourceUrl> selectAll();

    int updateByPrimaryKey(LitigationSourceUrl record);
}