package com.experian.daas.litigation.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.experian.daas.litigation.service.impl.LitigationAdapterServiceImpl.SerialCaseNumber;
import com.experian.dto.entity.litigation.LitigationParsedDataDetail;
import com.experian.dto.entity.litigation.LitigationParty;

public interface LitigationParsedDataDetailDao {
	int deleteByPrimaryKey(String id);

	int insert(LitigationParsedDataDetail record);

	LitigationParsedDataDetail selectByPrimaryKey(String id);

	List<LitigationParsedDataDetail> selectAll();

	int updateByPrimaryKey(LitigationParsedDataDetail record);

	List<LitigationParsedDataDetail> selectBySimplePreCaseNumber(@Param("caseNumber") String caseNumber,
			@Param("courtId") Integer courtId);

	List<LitigationParsedDataDetail> selectBySimpleCaseNumber(@Param("caseNumber") String caseNumber,
			@Param("courtId") Integer courtId);

	/**
	 * 案件编号获取诉讼主表数据
	 * 
	 * @param caseSerialNumber
	 * @return
	 */
	List<LitigationParsedDataDetail> selectByCaseSerialNumber(@Param("caseSerialNumber") String caseSerialNumber,
			@Param("courtId") Integer courtId);

	List<LitigationParsedDataDetail> selectBySupplierCaseSerialNumber(
			@Param("caseSerialNumber") String caseSerialNumber, @Param("courtId") Integer courtId);

	/**
	 * 案件编号，案号，上期案号获取诉讼主表数据
	 * 
	 * @param caseSerialNumber
	 *            案件编号
	 * @param caseNumber
	 *            案号
	 * @param previousCaseNumber
	 *            上期案号
	 * @return
	 */
	int selectByCaseSerialAndCaseNumberAndPreCaseNumber(@Param("caseSerialNumber") String caseSerialNumber,
			@Param("caseNumber") String caseNumber, @Param("previousCaseNumber") String previousCaseNumber);

	int getCountByCaseSerialNumber(String caseSerialNumber);

	/**
	 * 更新案件编号
	 * 
	 * @param originalSerialCaseNumber
	 *            需要更新的案件编号
	 * @param updatedSerialCaseNumber
	 *            更新后的案件编号
	 */
	void updateBySerialCaseNumber(@Param("original") String originalSerialCaseNumber,
			@Param("updated") String updatedSerialCaseNumber);

	List<LitigationParsedDataDetail> selectBySerialNumberAndAccuseDate(
			@Param("caseSerialNumber") String caseSerialNumber, @Param("accuseDate") Date accuseDate,
			@Param("courtId") Integer courtId);

	List<LitigationParsedDataDetail> selectBySupplierSerialNumberAndAccuseDate(
			@Param("caseSerialNumber") String caseSerialNumber, @Param("accuseDate") Date accuseDate);

	/**
	 * 批量更新
	 * 
	 * @param minSerialCaseNumber
	 *            更新后的案件编号
	 * @param serialCaseNumbers
	 *            需要更新的案件编号set
	 */
	void batchUpdateSerialCaseNumber(@Param("minSerialCaseNumber") String minSerialCaseNumber,
			@Param("serialCaseNumberObjs") Set<SerialCaseNumber> serialCaseNumberObjs);

	/**
	 * 
	 * @param dataForCompare
	 *            要的是新数据parsed_data_detail_id
	 * @param accuseDate
	 *            新数据案件审理日期
	 * @return
	 */
	List<LitigationParsedDataDetail> selectByAccuseDateInIds(@Param("data") List<LitigationParty> dataForCompare,
			@Param("accuseDate") Date accuseDate);

	/**
	 * 通过id批量查找
	 * 
	 * @param detailIds
	 */
	List<LitigationParsedDataDetail> selectByDetailIds(@Param("detailIds") List<String> detailIds);

	/**
	 * 有此案号的数量
	 * 
	 * @param caseNumber
	 * @return
	 */
	int getCountBySimpleCaseNumber(@Param("caseNumber") String caseNumber, @Param("courtId") Integer courtId);

	/**
	 * 匹配optix案件编号
	 * 
	 * @param caseSerialNumber
	 * @return
	 */
	int getCountBySupplierParsedDataId(String caseSerialNumber);

	List<LitigationParsedDataDetail> selectByCrawledDataId(String crawledDataId);

}