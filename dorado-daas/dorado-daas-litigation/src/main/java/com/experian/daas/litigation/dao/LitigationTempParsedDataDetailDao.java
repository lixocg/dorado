package com.experian.daas.litigation.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.experian.dto.entity.litigation.LitigationTempParsedDataDetail;

public interface LitigationTempParsedDataDetailDao {
	int deleteByPrimaryKey(String id);

	int insert(LitigationTempParsedDataDetail record);

	LitigationTempParsedDataDetail selectByPrimaryKey(String id);

	List<LitigationTempParsedDataDetail> selectAll();

	int updateByPrimaryKey(LitigationTempParsedDataDetail record);

	int selectByPkId(long pkid);

	void updateFetchFlagByPrimaryKey(@Param("id") String id, @Param("flag") int flag);

	List<LitigationTempParsedDataDetail> selectUnFetchData(@Param("fetchState") Integer fetchState,
			@Param("parsedStatus") Integer parsedStatus);

	LitigationTempParsedDataDetail selectUnFetchDataByPrimaryKey(@Param("id") String id,
			@Param("fetchState") Integer fetchState, @Param("parsedStatus") Integer parsedStatus);

	List<LitigationTempParsedDataDetail> selectLastest(@Param("num") long num, @Param("fetchState") Integer fetchState,
			@Param("parsedStatus") Integer parsedStatus);

	int getOldDataCount(@Param("fetchState") Integer fetchState, @Param("parsedStatus") Integer parsedStatus);

	/**
	 * 供应商id查询诉讼详情数据
	 * 
	 * @param detailSupplierId
	 *            供应商传送数据主键id
	 * @return
	 */
	List<LitigationTempParsedDataDetail> selectBySupplierId(String detailSupplierId);

}