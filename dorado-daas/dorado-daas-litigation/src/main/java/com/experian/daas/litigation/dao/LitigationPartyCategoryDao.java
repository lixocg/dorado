package com.experian.daas.litigation.dao;

import java.util.List;

import com.experian.dto.entity.litigation.LitigationPartyCategory;

public interface LitigationPartyCategoryDao {
    int deleteByPrimaryKey(Long id);

    int insert(LitigationPartyCategory record);

    LitigationPartyCategory selectByPrimaryKey(Integer id);

    List<LitigationPartyCategory> selectAll();

    int updateByPrimaryKey(LitigationPartyCategory record);
}