package com.experian.daas.litigation.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.experian.dto.entity.litigation.LitigationCaseCategory;

public interface LitigationCaseCategoryDao {
    int deleteByPrimaryKey(Long id);

    int insert(LitigationCaseCategory record);

    LitigationCaseCategory selectByPrimaryKey(@Param("id")Integer id);

    List<LitigationCaseCategory> selectAll();

    int updateByPrimaryKey(LitigationCaseCategory record);
    
    /**
     * @param code 案件分类
     * @return
     */
    LitigationCaseCategory selectByCode(String code);
}