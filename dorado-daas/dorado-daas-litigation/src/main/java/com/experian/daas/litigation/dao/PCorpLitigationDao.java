package com.experian.daas.litigation.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.experian.dto.entity.litigation.PCorpLitigation;

/**
 * @author e00898a
 *
 */
public interface PCorpLitigationDao {
	/**
	 * 获取诉讼平台记录总数
	 * 
	 * @return
	 */
	Integer getSqlserverCorpLitigationCount(@Param("updateDate")Date updateDate);

	/**
	 * 分页获取诉讼平台诉讼记录
	 * 
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	List<PCorpLitigation> getSqlserverCorpLitigationByPage(@Param("startRow") int startRow, @Param("endRow") int endRow,
			@Param("updateDate") Date updateDate);

}