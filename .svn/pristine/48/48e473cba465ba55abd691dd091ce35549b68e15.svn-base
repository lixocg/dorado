package com.experian.daas.litigation.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.experian.dto.entity.litigation.CorpLitigation;

public interface CorpLitigationDao {

	/**
	 * 获取未处理的诉讼主表记录总数
	 * 
	 * @return
	 */
	Integer getCorpLitigationCount(@Param("updateDate")Date updateDate);
	
	/**
	 * 分页获取未处理的诉讼主表记录
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	List<CorpLitigation> getCorpLitigationByPage(@Param("startRow") int startRow, @Param("endRow") int endRow,
			@Param("updateDate") Date updateDate);
}