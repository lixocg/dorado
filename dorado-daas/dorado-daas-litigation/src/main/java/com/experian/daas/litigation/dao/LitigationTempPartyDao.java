package com.experian.daas.litigation.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.experian.dto.entity.litigation.LitigationTempParty;

public interface LitigationTempPartyDao {
    int deleteByPrimaryKey(String id);

    int insert(LitigationTempParty record);

    LitigationTempParty selectByPrimaryKey(String id);

    List<LitigationTempParty> selectAll();

    int updateByPrimaryKey(LitigationTempParty record);

	List<LitigationTempParty> selectByDetailId(String detailId);

	/**
	 * 批量插入
	 * @param litigationTempPartyList
	 */
	void batchInset(@Param("litigationTempPartyList")List<LitigationTempParty> litigationTempPartyList);
}