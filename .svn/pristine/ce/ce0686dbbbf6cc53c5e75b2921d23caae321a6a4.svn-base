package com.experian.daas.baseinfo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.experian.dto.entity.baseinfo.CreditCorpRegistration;

public interface CreditCorpRegistrationDao {
	int deleteByPrimaryKey(String sbdnum);

	int insert(CreditCorpRegistration record);

	CreditCorpRegistration selectByPrimaryKey(String sbdnum);

	List<CreditCorpRegistration> selectAll();

	int updateByPrimaryKey(CreditCorpRegistration record);

	/**
	 * 根据sbdnums批量查询
	 * 
	 * @param sbdnums
	 * @param orderField
	 *            排序字段
	 * @return
	 */
	List<CreditCorpRegistration> selectBySbdnums(@Param("sbdnums") List<String> sbdnums,
			@Param("orderField") String orderField);
}