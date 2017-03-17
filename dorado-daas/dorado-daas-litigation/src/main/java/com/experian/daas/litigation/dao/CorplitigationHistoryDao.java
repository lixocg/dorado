package com.experian.daas.litigation.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.experian.dto.entity.litigation.CorplitigationHistory;

public interface CorplitigationHistoryDao {
	/**
	 * 获取进程表记录总数
	 * 
	 * @return
	 */
	Integer getLitigationHistroyCount(@Param("updateDate")Date updateDate);

	/**
	 * 分页获取进程表记录
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	List<CorplitigationHistory> getLitigationHistroyByPage(@Param("startRow") int startRow, @Param("endRow") int endRow,
			@Param("updateDate") Date updateDate);

}