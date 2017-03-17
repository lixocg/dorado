package com.experian.daas.litigation.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.experian.dto.entity.litigation.LitigationParsedDataDetail;
import com.experian.dto.entity.litigation.LitigationParty;

public interface LitigationPartyDao {
	int deleteByPrimaryKey(String id);

	int insert(LitigationParty record);

	LitigationParty selectByPrimaryKey(String id);

	List<LitigationParty> selectAll();

	int updateByPrimaryKey(LitigationParty record);

	/**
	 * 根据诉讼id统计企业自然人数量
	 * 
	 * @param parsedDataDetailId
	 * @return
	 */
	int getCountByParsedDataDetailId(String parsedDataDetailId);

	/**
	 * /** 根据诉讼id统计企业自然人
	 * 
	 * @param parsedDataDetailId
	 * @return
	 */
	List<LitigationParty> selectByParsedDataDetailId(String parsedDataDetailId);

	/**
	 * 批量删除
	 * 
	 * @param partiesOfOld
	 */
	void batchDetele(List<LitigationParty> partiesOfOld);

	/**
	 * 批量插入
	 * 
	 * @param paties
	 */
	void batchInsert(@Param("parties") List<LitigationParty> parties);

	/**
	 * 批量查询企业原被告当事人数量，按降序排序
	 * 
	 * @param litis
	 * @return
	 */
	List<Map<String, Object>> batchSelectAccPartyDescOrder(@Param("litis") List<LitigationParsedDataDetail> litis);

	/**
	 * 批量查询有sbdnum的数量，按降序排序
	 * 
	 * @param litis
	 * @return
	 */
	List<Map<String, Object>> batchSelectSbdnumDescOrder(@Param("litis") List<LitigationParsedDataDetail> litis);

	/**
	 * 通过自然人集合查找有案号或者上期案号的自然人数据
	 * 
	 * @param partyNames(自然人名称)
	 * @param nullCode(上期案号或者案号不为空)1、不为空2、都为空
	 * @return
	 */
	List<LitigationParty> selectByPartyList(@Param("parties") List<LitigationParty> parties,@Param("courtId")Integer courtId,
			@Param("nullCode") int nullCode);

	/**
	 * 通过详情id删除
	 * 
	 * @param detailId
	 */
	void deleteByDetailId(String detailId);

	List<LitigationParty> selectByPartyListWithSimpleCaseNumber(@Param("parties") List<LitigationParty> parties,
			@Param("caseNumber") String caseNumber, @Param("serialCaseNumber") String serialCaseNumber,
			@Param("courtId") Integer courtId);

	/**
	 * 更新sbdnum
	 * 
	 * @param sbdnum
	 * @param id
	 */
	void updateSbdnumByPrimaryKey(@Param("sbdnum") String sbdnum, @Param("id") String id);
}