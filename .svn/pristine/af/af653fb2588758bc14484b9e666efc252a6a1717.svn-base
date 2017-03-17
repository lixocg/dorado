package com.experian.daas.litigation.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.experian.dto.entity.litigation.LitigationErrorParty;

public interface LitigationErrorPartyDao {
	int deleteByPrimaryKey(String id);

	int insert(LitigationErrorParty record);

	LitigationErrorParty selectByPrimaryKey(String id);

	List<LitigationErrorParty> selectAll();

	int updateByPrimaryKey(LitigationErrorParty record);

	void batchInsert(@Param("parties") List<LitigationErrorParty> parties);
}