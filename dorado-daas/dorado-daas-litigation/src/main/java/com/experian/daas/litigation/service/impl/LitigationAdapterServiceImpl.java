package com.experian.daas.litigation.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.experian.comp.rabbitmq.RabbitClient;
import com.experian.comp.rabbitmq.StringConsumer;
import com.experian.core.constant.LitiBusiConstant;
import com.experian.core.enums.CategorySort;
import com.experian.core.utils.CiperUtil;
import com.experian.daas.litigation.config.ConstantConfig;
import com.experian.daas.litigation.dao.LitigationCaseCategoryDao;
import com.experian.daas.litigation.dao.LitigationCourtDao;
import com.experian.daas.litigation.dao.LitigationCrawledDataDao;
import com.experian.daas.litigation.dao.LitigationErrorCrawledDataDao;
import com.experian.daas.litigation.dao.LitigationErrorParsedDataDetailDao;
import com.experian.daas.litigation.dao.LitigationErrorPartyDao;
import com.experian.daas.litigation.dao.LitigationParsedDataDetailDao;
import com.experian.daas.litigation.dao.LitigationPartyDao;
import com.experian.daas.litigation.dao.LitigationSourceUrlDao;
import com.experian.daas.litigation.dao.LitigationTempCrawledDataDao;
import com.experian.daas.litigation.dao.LitigationTempParsedDataDetailDao;
import com.experian.daas.litigation.dao.LitigationTempPartyDao;
import com.experian.daas.litigation.handler.DeplicateHandler;
import com.experian.daas.litigation.handler.MatchSbdnumHandler;
import com.experian.daas.litigation.service.LitigationAdapterService;
import com.experian.daas.litigation.service.LitigationToMonitorService;
import com.experian.dto.entity.litigation.LitigationCrawledData;
import com.experian.dto.entity.litigation.LitigationErrorCrawledData;
import com.experian.dto.entity.litigation.LitigationErrorParsedDataDetail;
import com.experian.dto.entity.litigation.LitigationErrorParty;
import com.experian.dto.entity.litigation.LitigationParsedDataDetail;
import com.experian.dto.entity.litigation.LitigationParty;
import com.experian.dto.entity.litigation.LitigationTempCrawledData;
import com.experian.dto.entity.litigation.LitigationTempParsedDataDetail;
import com.experian.dto.entity.litigation.LitigationTempParty;
import com.experian.dto.pojo.litigation.LitigationAPIPojo;
import com.experian.dto.pojo.litigation.LitigationDetailPojo;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service("litigationAdapterService")
public class LitigationAdapterServiceImpl implements LitigationAdapterService {
	private static final Logger logger = Logger.getLogger(LitigationAdapterServiceImpl.class);

	@Resource
	private LitigationParsedDataDetailDao parsedDataDetailDao;

	@Resource
	private LitigationPartyDao partyDao;

	@Resource
	private LitigationCrawledDataDao crawledDataDao;

	@Resource
	private LitigationTempParsedDataDetailDao tempParsedDataDetailDao;

	@Resource
	private LitigationTempCrawledDataDao tempCrawledDataDao;

	@Resource
	private LitigationTempPartyDao tempPartyDao;

	@Resource
	private LitigationErrorParsedDataDetailDao errorParsedDataDetailDao;

	@Resource
	private LitigationErrorCrawledDataDao errorCrawledDataDao;

	@Resource
	private LitigationErrorPartyDao errorPartyDao;

	@Resource
	private LitigationCaseCategoryDao caseCategoryDao;

	@Resource
	private LitigationCourtDao courtDao;

	@Resource
	private LitigationSourceUrlDao sourceUrlDao;

	@Autowired
	private LitigationToMonitorService litigationToMonitorService;

	private static Gson gson = new Gson();

	StringBuffer sb = null;

	@Autowired
	private RabbitClient rabbit;

	@PostConstruct
	public void startConsumerJob() throws Exception {
		logger.info("开启去重消费端........");
		for (int i = 0; i < ConstantConfig.Queue.QUEUE_SIZE; i++) {
			rabbit.reveiceMsg(ConstantConfig.Queue.QUEUE_NAME_PREFIX + i,
					new StringConsumer(new DeplicateHandler(this)));
		}

		logger.info("开启匹配sbdnum消费端........");
		rabbit.reveiceMsg(ConstantConfig.Queue.MATCH_SBDNUM_QUEUE,
				new StringConsumer(new MatchSbdnumHandler(partyDao)));
	}

	/**
	 * 去重入口
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public String executeAdapt(LitigationAPIPojo litigation) {
		sb = new StringBuffer();
		LitigationParsedDataDetail parsedDataDetail = litigation.getLitigationParsedDataDetail();
		String oldId = parsedDataDetail.getId();
		Integer courtId = parsedDataDetail.getCourtId();
		List<LitigationParty> parties = litigation.getLitigationPartyList();
		LitigationCrawledData crawledData = litigation.getLitigationCrawledData();

		dataValidate(parsedDataDetail, crawledData, parties);

		try {
			logger.info("去重<<<<<<<<<<<<<开始>>>>" + oldId + ">>>>>>>>>");
			long start = System.currentTimeMillis();
			String caseSerialNumber = parsedDataDetail.getParsedDataId();
			/*** 检查库中是否有新数据的案件编号 ****/
			String experianCaseSerialNumber = containsCaseSerialNumber(caseSerialNumber, courtId);
			if (StringUtils.isNotEmpty(experianCaseSerialNumber)) {
				/*** 对应原有库中在此案件编号下的案号和上期案号是否包含了新数据的案号和上期案号 ***/
				if (containsCaseNumberAndPreCaseNumber(experianCaseSerialNumber, parsedDataDetail.getSimpleCaseNumber(),
						parsedDataDetail.getSimplePreviousCaseNumber(), courtId)) {
					/*** 库中有新数据案件编号，并且有新数据案号和上期案号 去重 **/
					deduplicateWithMatchedSerialNumber(experianCaseSerialNumber, parsedDataDetail, parties,
							crawledData);
				} else {
					/*** 库中有新数据案件编号，但没有新数据案号和上期案号 去重 **/
					deduplicateWithOutMatchedSerialNumber(parsedDataDetail, parties, crawledData);
				}
			} else {
				/*** 库中没有新数据的案件编号 ****/
				deduplicateWithOutMatchedSerialNumber(parsedDataDetail, parties, crawledData);
			}

			// 更新临时表数据拉取状态
			// tempParsedDataDetailDao.updateFetchFlagByPrimaryKey(oldId,
			// LitiBusiConstant.DataFetchStatus.FETCHED);
			logger.info("去重<<<<<<<<<<<<<结束>>>>" + oldId + ">>>>>>>>>耗时:" + (System.currentTimeMillis() - start) + "毫秒");
			sb.append(
					"\r\n去重<<<<<<<<<<<<<结束>>>>>" + oldId + ">>>>>>>>耗时:" + (System.currentTimeMillis() - start) + "毫秒");
		} catch (Exception e) {
			logger.error("去重发生错误:" + e);
			// 落入错误表
			insertError(LitiBusiConstant.ErrorType.dataNotCriteria.ordinal(), parsedDataDetail, crawledData, parties);
		}
		return sb.toString();
	}

	/**
	 * 去重之前数据检查
	 * 
	 * @param parsedDataDetail
	 * @param crawledData
	 * @param parties
	 */
	private void dataValidate(LitigationParsedDataDetail parsedDataDetail, LitigationCrawledData crawledData,
			List<LitigationParty> parties) {
		// TODO Auto-generated method stub

	}

	/**
	 * 落入异常表
	 * 
	 * @param errorType
	 * @param parsedDataDetail
	 * @param crawledData
	 * @param parties
	 */
	private void insertError(int errorType, LitigationParsedDataDetail parsedDataDetail,
			LitigationCrawledData crawledData, List<LitigationParty> parties) {
		if (!LitiBusiConstant.ErrorType.contains(errorType)) {
			logger.error("无此errorType:" + errorType);
			return;
		}
		logger.info("<<<<<<<<<<<<<落error表开始>>>>>>>>>>>>:诉讼详情id:" + parsedDataDetail.getId());
		LitigationErrorParsedDataDetail errorParsedDataDetail = new LitigationErrorParsedDataDetail();
		LitigationErrorCrawledData errorCrawledData = new LitigationErrorCrawledData();
		List<LitigationErrorParty> errorParties = null;
		try {
			BeanUtils.copyProperties(errorParsedDataDetail, parsedDataDetail);
			BeanUtils.copyProperties(errorCrawledData, crawledData);
			if (!CollectionUtils.isEmpty(parties)) {
				errorParties = new ArrayList<LitigationErrorParty>();
				for (LitigationParty party : parties) {
					LitigationErrorParty errorParty = new LitigationErrorParty();
					BeanUtils.copyProperties(errorParty, party);
					errorParty.setInsertDate(new Date());
					errorParties.add(errorParty);
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		errorParsedDataDetail.setErrorType(errorType);
		errorParsedDataDetail.setInsertDate(new Date());
		errorParsedDataDetailDao.insert(errorParsedDataDetail);
		if (errorParties != null) {
			errorPartyDao.batchInsert(errorParties);
		}

		String errorCrawledid = errorCrawledData.getId();
		LitigationErrorCrawledData errorCrawledDataOfExist = errorCrawledDataDao.selectByPrimaryKey(errorCrawledid);
		if (errorCrawledDataOfExist == null) {
			errorCrawledData.setInsertDate(new Date());
			errorCrawledDataDao.insert(errorCrawledData);
		}
		// tempParsedDataDetailDao.updateFetchFlagByPrimaryKey(parsedDataDetail.getId(),
		// LitiBusiConstant.DataFetchStatus.RE_FETCH);
		logger.info("<<<<<<<<<<<<<落error表结束>>>>>>>>>>>>:诉讼详情id:" + parsedDataDetail.getId());
	}

	/**
	 * 对应原有库中在此案件编号下的案号和上期案号是否包含了新数据的案号和案件编号
	 * 
	 * @param caseSerialNumber
	 * @param caseNumber
	 *            案号
	 * @param preCaseNUmber
	 *            上期案号
	 * @return
	 */
	private boolean containsCaseNumberAndPreCaseNumber(String caseSerialNumber, String caseNumber, String preCaseNUmber,
			Integer courtId) {
		List<LitigationParsedDataDetail> details = parsedDataDetailDao.selectByCaseSerialNumber(caseSerialNumber,
				courtId);
		if (CollectionUtils.isEmpty(details)) {
			return false;
		}
		// 案号和上期案号都为空，包含
		if (StringUtils.isEmpty(caseNumber) && StringUtils.isEmpty(preCaseNUmber)) {
			return true;
		}
		Set<String> existCaseNumberAndPreCaseNumbers = new HashSet<String>();
		for (LitigationParsedDataDetail detail : details) {
			String exitsCaseNumber = detail.getSimpleCaseNumber();
			String existPrecaseNumber = detail.getSimplePreviousCaseNumber();
			if (StringUtils.isNotBlank(exitsCaseNumber)) {
				existCaseNumberAndPreCaseNumbers.add(exitsCaseNumber);
			}
			if (StringUtils.isNotBlank(existPrecaseNumber)) {
				existCaseNumberAndPreCaseNumbers.add(existPrecaseNumber);
			}
		}
		Set<String> newCaseNumberAndPreCaseNumbers = new HashSet<String>();
		if (StringUtils.isNotBlank(caseNumber)) {
			newCaseNumberAndPreCaseNumbers.add(caseNumber);
		}
		if (StringUtils.isNotEmpty(preCaseNUmber)) {
			newCaseNumberAndPreCaseNumbers.add(preCaseNUmber);
		}
		return existCaseNumberAndPreCaseNumbers.containsAll(newCaseNumberAndPreCaseNumbers);
	}

	/**
	 * 与库中案件编号匹配
	 * 
	 * @param caseSerialNumber
	 *            新诉讼案件编号
	 * @return experian的案件编号
	 */
	public String containsCaseSerialNumber(String caseSerialNumber, Integer courtId) {
		if (StringUtils.isEmpty(caseSerialNumber)) {
			return null;
		}
		List<LitigationParsedDataDetail> detail = parsedDataDetailDao.selectBySupplierCaseSerialNumber(caseSerialNumber,
				courtId);
		if (CollectionUtils.isEmpty(detail)) {
			return null;
		}
		return detail.get(0).getParsedDataId();
	}

	/**
	 * 库中有新数据案件编号的去重
	 * 
	 * @param caseSerialNumber
	 *            案件编号
	 * @param parsedDataDetail
	 *            新数据诉讼主表
	 * @throws Exception
	 */
	private void deduplicateWithMatchedSerialNumber(String caseSerialNumber,
			LitigationParsedDataDetail parsedDataDetail, List<LitigationParty> parties,
			LitigationCrawledData crawledData) throws Exception {
		// 检查同案件编号下，案件审理时间相同的数据
		List<LitigationParsedDataDetail> litis = parsedDataDetailDao.selectBySerialNumberAndAccuseDate(caseSerialNumber,
				parsedDataDetail.getAccuseDate(), parsedDataDetail.getCourtId());
		if (CollectionUtils.isEmpty(litis)) {
			// 直接插入
			saveWithSerialCaseNumber(caseSerialNumber, parsedDataDetail, parties, crawledData);
		} else {
			// 找出案件状态相同的数据
			List<LitigationParsedDataDetail> sameAccuseStatusList = findSameAccuseStatusData(parsedDataDetail, litis);
			if (!CollectionUtils.isEmpty(sameAccuseStatusList)) {
				// 按照规则保留完备性高的数据
				insertByBusiness(parsedDataDetail, litis, parties, crawledData);
			} else {
				saveWithSerialCaseNumber(caseSerialNumber, parsedDataDetail, parties, crawledData);
			}
		}
	}

	/**
	 * 新数据入新库---使用匹配到的案件编号
	 * 
	 * @param newParsedDataDetail
	 * @throws Exception
	 */
	private void saveWithSerialCaseNumber(String serialCaseNumber, LitigationParsedDataDetail newParsedDataDetail,
			List<LitigationParty> parties, LitigationCrawledData crawledData) throws Exception {
		save(newParsedDataDetail, parties, crawledData, serialCaseNumber);
	}

	/**
	 * 保存
	 * 
	 * @param newParsedDataDetail
	 * @param parties
	 * @param crawledData
	 * @param serialCaseNumber
	 *            不使用原来数据案件编号 传null
	 * @throws Exception
	 */
	private void save(LitigationParsedDataDetail newParsedDataDetail, List<LitigationParty> parties,
			LitigationCrawledData crawledData, String serialCaseNumber) throws Exception {
		logger.info("新数据入库<<<<<<<<<<开始>>>>>>>>>>>>>>");
		String supplierParsedDataId = newParsedDataDetail.getParsedDataId();
		try {
			if (StringUtils.isNotBlank(serialCaseNumber)) {
				newParsedDataDetail.setParsedDataId(serialCaseNumber);
			}
			newParsedDataDetail.setSupplierParsedDataId(supplierParsedDataId);
			newParsedDataDetail.setInsertDate(new Date());
			if (newParsedDataDetail.getUpdatedDate() == null) {
				newParsedDataDetail.setUpdatedDate(new Date());
			}
			parsedDataDetailDao.insert(newParsedDataDetail);
			logger.info("插入诉讼详情表成功，id=" + newParsedDataDetail.getId());

			// 插入企业自然人表，生成新企业自然人主键id，设置刚才生成详情主键
			if (!CollectionUtils.isEmpty(parties)) {
				String ids = "";
				for (LitigationParty p : parties) {
					ids += p.getId() + ",";
					p.setInsertDate(new Date());
					// 加密
					if (StringUtils.isNotEmpty(p.getIdentificationNumber())) {
						p.setIdentificationNumber(
								CiperUtil.encrypt(CiperUtil.SECURITY_KEY, p.getIdentificationNumber()));
					}
					if (StringUtils.isNotEmpty(p.getDateOfBirth())) {
						p.setDateOfBirth(CiperUtil.encrypt(CiperUtil.SECURITY_KEY, p.getDateOfBirth()));
					}
				}
				partyDao.batchInsert(parties);
				logger.info("批量插入企业自然人表成功，id=(" + ids.substring(0, ids.length() - 1) + ")");
				// 匹配sbdnum
				rabbit.sendMsg(ConstantConfig.Queue.MATCH_SBDNUM_QUEUE, gson.toJson(parties));
			}

			// 插入抓去数据表
			LitigationCrawledData crawledDataOfExist = crawledDataDao.selectByPrimaryKey(crawledData.getId());
			if (crawledDataOfExist == null) {
				crawledDataDao.insert(crawledData);
			} else {
				crawledDataDao.updateByPrimaryKey(crawledData);
			}
			logger.info("插入抓取数据表成功，id=" + crawledData.getId());
		} catch (Exception e) {
			logger.error(e);
			return;
		}
		logger.info("新数据入库<<<<<<<<<<结束>>>>>>>>>>>>>>");
		//将此数据丢进要给诉讼监测使用的消息队列中
		logger.info("丢经消息队列开始");
		long start = System.currentTimeMillis();
		litigationToMonitorService.transLitigationTo(new LitigationAPIPojo(crawledData, newParsedDataDetail, parties));
		logger.info(String.format("丢入消息队列结束，耗时:%s", (System.currentTimeMillis()-start)));
	}

	/**
	 * optix判重
	 * 
	 * @param tempParsedDataDetail
	 * @throws Exception
	 */
	public void deduplicateWithOutMatchedSerialNumber(LitigationParsedDataDetail parsedDataDetail,
			List<LitigationParty> parties, LitigationCrawledData crawledData) throws Exception {
		String caseNumber = parsedDataDetail.getSimpleCaseNumber();
		String preCaseNumber = parsedDataDetail.getSimplePreviousCaseNumber();
		/***************** 是否有案号或上期案号 ******************************/
		if (StringUtils.isNotBlank(caseNumber) || StringUtils.isNotBlank(preCaseNumber)) {
			hasCaseNumberOrPreCaseNumber(parsedDataDetail, parties, crawledData);
		} else {// 没有案号和上期案号
			noCaseNumberAndPreCaseNumber(parsedDataDetail, parties, crawledData);
		}
	}

	/**
	 * 有案号或上期案号
	 * 
	 * @param parsedDataDetail
	 * @param parties
	 * @throws Exception
	 */
	private void hasCaseNumberOrPreCaseNumber(LitigationParsedDataDetail parsedDataDetail,
			List<LitigationParty> parties, LitigationCrawledData crawledData) throws Exception {
		String caseNumber = parsedDataDetail.getSimpleCaseNumber();
		String preCaseNumber = parsedDataDetail.getSimplePreviousCaseNumber();
		Integer courtId = parsedDataDetail.getCourtId();
		// 案号或上期案号诉讼
		Set<SerialCaseNumber> serialCaseNumberObjs = matchWithCaseNumberOrPreCaseNumber(caseNumber, preCaseNumber,
				courtId);
		String minSerialCaseNumber = null;
		/************ 匹配案件编号 *********************/
		if (serialCaseNumberObjs != null && serialCaseNumberObjs.size() > 0) {// 匹配到案件编号
			if (serialCaseNumberObjs.size() > 1) {// 匹配上的案件编号不相同
				// 合并到较小的案件编号下
				// 批量更新案件编号为第一个
				SerialCaseNumber minSerialCaseNumberObj = serialCaseNumberObjs.iterator().next();
				minSerialCaseNumber = minSerialCaseNumberObj.value;
				serialCaseNumberObjs.remove(minSerialCaseNumberObj);
				parsedDataDetailDao.batchUpdateSerialCaseNumber(minSerialCaseNumber, serialCaseNumberObjs);
			} else {// 匹配的案件编号相同
				minSerialCaseNumber = serialCaseNumberObjs.iterator().next().value;
			}
			// 开始去重操作
			if (caseNumberIsNullOrContainsTheCaseNumber(caseNumber, courtId)) {// 案号为空
				// 且原有数据包括此案号
				// 在此案号（可能会是空案号）下原被告当事人是否均不相同
				/**
				 * 有案号或案号为空，并且原被告当事人与来源数据的原被告当事人匹配
				 */
				List<LitigationParty> possibleParty = partyDao.selectByPartyListWithSimpleCaseNumber(parties,
						caseNumber, minSerialCaseNumber, courtId);
				// 查找到了
				if (possibleParty != null && possibleParty.size() != 0) {
					// 按照诉讼详情id+案件审理时间进行分组
					Map<String, List<LitigationParty>> map = new HashMap<String, List<LitigationParty>>();
					for (LitigationParty p : possibleParty) {
						String mapKey = p.getParsedDataDetailId() + "_"
								+ (p.getAccuseDate() == null ? "" : p.getAccuseDate().getTime());
						// 没有存过
						if (map.get(mapKey) == null) {
							List<LitigationParty> lps = new ArrayList<LitigationParty>();
							lps.add(p);
							map.put(mapKey, lps);
						} else {
							map.get(mapKey).add(p);
						}
					}
					Map<String, List<LitigationParty>> hasSameMap = findAccuserOrAccusedOrPartySame(parties, map);
					if (hasSameMap.size() == 0) {// 原被告当事人均不相同
						contains执字(parsedDataDetail, parties, crawledData, minSerialCaseNumber);
					} else {// 原被告当事人至少有一个相同
						matchAccuseDate(parsedDataDetail, hasSameMap, parties, crawledData);
					}
				} else {// 原被告当事人均不相同
					contains执字(parsedDataDetail, parties, crawledData, minSerialCaseNumber);
				}
			} else {
				// 新插入，使用相同的案件编号
				saveWithSerialCaseNumber(minSerialCaseNumber, parsedDataDetail, parties, crawledData);
			}
		} else {// 没有匹配上案件编号
			noCaseNumberAndPreCaseNumberNext(parsedDataDetail, parties, crawledData);
		}
	}

	/**
	 * 案号中包含执字
	 * 
	 * @param parsedDataDetail
	 * @param parties
	 * @param crawledData
	 * @param minSerialCaseNumber
	 * @throws Exception
	 */
	private void contains执字(LitigationParsedDataDetail parsedDataDetail, List<LitigationParty> parties,
			LitigationCrawledData crawledData, String minSerialCaseNumber) throws Exception {
		// 案号中是否包括"执"字
		if (parsedDataDetail.getSimpleCaseNumber().contains("执")) {// 包括
			// 插入到诉讼库中，使用相同的案件编号
			saveWithSerialCaseNumber(minSerialCaseNumber.toString(), parsedDataDetail, parties, crawledData);
		} else {// 不包括
				// 与无案号和上期案号判重***************
			noCaseNumberAndPreCaseNumberNext(parsedDataDetail, parties, crawledData);

		}
	}

	/**
	 * 在已分组数据中找出原被告当事人相同的数据
	 * 
	 * @param parties
	 * @param map
	 * @return
	 */
	private Map<String, List<LitigationParty>> findAccuserOrAccusedOrPartySame(List<LitigationParty> parties,
			Map<String, List<LitigationParty>> map) {
		// 有原被告当事人相同的map
		Map<String, List<LitigationParty>> hasSameMap = new HashMap<String, List<LitigationParty>>();
		// 便利所有可疑自然人，只要发现与来源的自然人不同，就把该诉讼详情删除
		Iterator<Map.Entry<String, List<LitigationParty>>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, List<LitigationParty>> entry = it.next();
			List<LitigationParty> partyByDetailList = entry.getValue();
			List<String> accusers = new ArrayList<String>();// 原告
			List<String> accuseds = new ArrayList<String>();// 被告
			List<String> partys = new ArrayList<String>();// 当事人
			for (LitigationParty p : partyByDetailList) {
				Integer partyCategoryId = p.getPartyCategoryId();
				String name = p.getName();
				if (StringUtils.isNotBlank(name)) {
					if (LitiBusiConstant.PartyCategory.ACCUSER == partyCategoryId) {
						accusers.add(name);
					} else if (LitiBusiConstant.PartyCategory.ACCUSED == partyCategoryId) {
						accuseds.add(name);
					} else if (LitiBusiConstant.PartyCategory.PARTIES == partyCategoryId) {
						partys.add(name);
					}
				}
			}

			List<String> accusersOfOrigin = new ArrayList<String>();
			List<String> accusedsOfOrigin = new ArrayList<String>();
			List<String> partissOfOrigin = new ArrayList<String>();
			for (LitigationParty pn : parties) {
				String name = pn.getName();
				if (StringUtils.isNotBlank(name)) {
					if (LitiBusiConstant.PartyCategory.ACCUSER == pn.getPartyCategoryId()) {// 原告
						accusersOfOrigin.add(name);
					} else if (LitiBusiConstant.PartyCategory.ACCUSED == pn.getPartyCategoryId()) {// 被告
						accusedsOfOrigin.add(name);
					} else if (LitiBusiConstant.PartyCategory.PARTIES == pn.getPartyCategoryId()) {// 当事人
						partissOfOrigin.add(name);
					}
				}
			}

			if (isAccuserOrAccusedOrPartyTheSame(accusersOfOrigin, accusers)
					|| isAccuserOrAccusedOrPartyTheSame(accusedsOfOrigin, accuseds)
					|| isAccuserOrAccusedOrPartyTheSame(partissOfOrigin, partys)) {
				hasSameMap.put(entry.getKey(), entry.getValue());
			}
		}
		return hasSameMap;
	}

	/**
	 * 原被告当事人是否相等
	 * 
	 * @param newData
	 *            新的原被告当事人
	 * @param oldData
	 *            原有数据原被告当事人
	 * @return
	 */
	private boolean isAccuserOrAccusedOrPartyTheSame(List<String> newData, List<String> oldData) {
		if (newData == null && oldData == null) {
			return true;
		}
		if ((newData.size() == 0) && (oldData.size() == newData.size())) {
			return false;
		}
		int size = intersect(newData, oldData).size();
		return size > 0;
	}

	/**
	 * 找交集，千万不要用retainAll方法，坑爹的
	 * 
	 * @param o
	 * @param d
	 * @return
	 */
	private List<String> intersect(List<String> o, List<String> d) {
		List<String> intersection = new ArrayList<String>();
		int osize = o.size();
		int dsize = o.size();
		if (osize > dsize) {
			for (String dd : d) {// 循环小的
				for (String oo : o) {
					if (dd.equals(oo)) {
						intersection.add(dd);
					}
				}
			}
		} else {
			for (String oo : o) {
				for (String dd : d) {
					if (oo.equals(dd)) {
						intersection.add(oo);
					}
				}
			}
		}
		return intersection;
	}

	private boolean caseNumberIsNullOrContainsTheCaseNumber(String caseNumber, Integer courtId) {
		if (StringUtils.isBlank(caseNumber)) {
			return true;
		}
		int count = parsedDataDetailDao.getCountBySimpleCaseNumber(caseNumber, courtId);
		return count > 0;
	}

	/**
	 * 与有案号或上期案号 匹配案件编号
	 * 
	 * @param caseNumber
	 *            新数据案号
	 * @param preCaseNumber
	 *            新数据上期案号
	 * @return
	 */
	private Set<SerialCaseNumber> matchWithCaseNumberOrPreCaseNumber(String caseNumber, String preCaseNumber,
			Integer courtId) {
		Set<SerialCaseNumber> caseSerialNumber = new TreeSet<SerialCaseNumber>();// TreeSet天生唯一自然升序，不要用String类型
		if (StringUtils.isNotBlank(caseNumber)) {
			// 新诉讼案号与已有诉讼的非空上期案号匹配
			List<LitigationParsedDataDetail> data = parsedDataDetailDao.selectBySimplePreCaseNumber(caseNumber,
					courtId);
			if (!CollectionUtils.isEmpty(data)) {
				for (LitigationParsedDataDetail pdl : data) {
					caseSerialNumber.add(new SerialCaseNumber(pdl.getParsedDataId()));
				}
			}
			// 新诉讼案号与已有诉讼非空案号匹配
			List<LitigationParsedDataDetail> data1 = parsedDataDetailDao.selectBySimpleCaseNumber(caseNumber, courtId);
			if (!CollectionUtils.isEmpty(data1)) {
				for (LitigationParsedDataDetail pdl : data1) {
					caseSerialNumber.add(new SerialCaseNumber(pdl.getParsedDataId()));
				}
			}
		}
		if (StringUtils.isNotBlank(preCaseNumber)) {
			// 新诉讼上期案号与已有诉讼非空案号匹配
			List<LitigationParsedDataDetail> data1 = parsedDataDetailDao.selectBySimpleCaseNumber(preCaseNumber,
					courtId);
			if (!CollectionUtils.isEmpty(data1)) {
				for (LitigationParsedDataDetail pdl : data1) {
					caseSerialNumber.add(new SerialCaseNumber(pdl.getParsedDataId()));
				}
			}
			// 新诉讼上期案号与诉讼的非空上期案号匹配
			List<LitigationParsedDataDetail> data = parsedDataDetailDao.selectBySimplePreCaseNumber(preCaseNumber,
					courtId);
			if (!CollectionUtils.isEmpty(data)) {
				for (LitigationParsedDataDetail pdl : data) {
					caseSerialNumber.add(new SerialCaseNumber(pdl.getParsedDataId()));
				}
			}
		}

		return caseSerialNumber;
	}

	/**
	 * 是否有至少两个案件包含执字
	 * 
	 * @param list
	 * @return
	 */
	private boolean hasTowMoreZhiCaseNumber(List<LitigationParsedDataDetail> list) {
		int count = 0;
		for (LitigationParsedDataDetail p : list) {
			String caseNumber = p.getSimpleCaseNumber();
			if (StringUtils.isNotEmpty(caseNumber) && caseNumber.contains("执")) {
				count++;
			}
		}
		return count > 1;
	}

	/**
	 * 按照规则保留完备性高的数据进行入库
	 * 
	 * @param from(来源的诉讼)
	 * @param list(现有的诉讼)
	 * @throws Exception
	 */
	public void insertByBusiness(LitigationParsedDataDetail from, List<LitigationParsedDataDetail> list,
			List<LitigationParty> parties, LitigationCrawledData crawledData) throws Exception {
		if (CollectionUtils.isEmpty(list)) {
			return;
		}
		if (!hasTowMoreZhiCaseNumber(list)) {
			if (list.size() != 1) {
				logger.error("不具完备性检测，检测数据长度:" + list.size());
				return;
			}
		}

		// 需要更新到的案件编号
		String serialCaseNumber = list.get(0).getParsedDataId();
		boolean isStop = false;// 是否停止完备性检测
		// 物理删除库中不具有完备性的数据，当且仅当新来数据还存在时才能删除
		List<LitigationParsedDataDetail> todoDelList = new ArrayList<LitigationParsedDataDetail>();
		/**
		 * 将来源的与现有的进行合并到待处理集合
		 */
		List<LitigationDetailPojo> todoList = new ArrayList<LitigationDetailPojo>();
		LitigationDetailPojo pojo = new LitigationDetailPojo(from, LitigationDetailPojo.FROM);
		todoList.add(pojo);
		for (LitigationParsedDataDetail detail : list) {
			todoList.add(new LitigationDetailPojo(detail, LitigationDetailPojo.HISTORY));
		}
		/**
		 * 1、 优先保留有案号的规则
		 */
		if (todoList.size() > 1) {
			isStop = removeNoCaseNumber(todoList, todoDelList);
		} else {
			if (todoList.size() == 1 && LitigationDetailPojo.FROM.equals(todoList.get(0).getType())) {
				// 插入新数据
				saveWithSerialCaseNumber(serialCaseNumber, from, parties, crawledData);
			}
			return;
		}

		// 是否停止完备性检测
		if (isStop) {
			return;
		} else {
			// 物理删除库中不具完备性数据，并清空todoDelList
			if (!CollectionUtils.isEmpty(todoDelList)) {
				deleteHistory(todoDelList);
				todoDelList.clear();
			}
		}

		/**
		 * 2、保留做过报告的(只对旧数据)规则
		 */
		// 是否不仅有一条诉讼
		if (from.getDataFrom() != 0) {
			if (todoList.size() > 1) {
				isStop = removeNoCourt(todoList, todoDelList);
			} else {// 仅剩一条诉讼
				if (todoList.size() == 1 && LitigationDetailPojo.FROM.equals(todoList.get(0).getType())) {
					// 插入新数据
					saveWithSerialCaseNumber(serialCaseNumber, from, parties, crawledData);
				}
				return;
			}

			// 是否停止完备性检测
			if (isStop) {
				return;
			} else {
				// 物理删除库中不具完备性数据，并清空todoDelList
				if (!CollectionUtils.isEmpty(todoDelList)) {
					deleteHistory(todoDelList);
					todoDelList.clear();
				}
			}
		}

		/**
		 * 3、 都有或者都无案号规则
		 */
		if (todoList.size() > 1) {
			isStop = removeCaseCategory(todoList, todoDelList);
		} else {
			if (todoList.size() == 1 && LitigationDetailPojo.FROM.equals(todoList.get(0).getType())) {
				saveWithSerialCaseNumber(serialCaseNumber, from, parties, crawledData);
			}
			return;
		}

		// 是否停止完备性检测
		if (isStop) {
			return;
		} else {
			// 物理删除库中不具完备性数据，并清空todoDelList
			if (!CollectionUtils.isEmpty(todoDelList)) {
				deleteHistory(todoDelList);
				todoDelList.clear();
			}
		}

		/**
		 * 4、optix数据，保留原被告当事人多的 ；旧数据，保留sbdnum多的
		 */
		if (todoList.size() > 1) {
			isStop = removeParty(todoList, from, parties, crawledData, todoDelList);
		} else {
			if (todoList.size() == 1 && LitigationDetailPojo.FROM.equals(todoList.get(0).getType())) {
				saveWithSerialCaseNumber(serialCaseNumber, from, parties, crawledData);
			}
			return;
		}

		// 是否停止完备性检测
		if (isStop) {
			return;
		} else {
			// 物理删除库中不具完备性数据，并清空todoDelList
			if (!CollectionUtils.isEmpty(todoDelList)) {
				deleteHistory(todoDelList);
				todoDelList.clear();
			}
		}

		/**
		 * 5.保留更新时间最新那条
		 */
		if (todoList.size() > 1) {
			isStop = removeNotLastestUpdatetime(todoList, from, todoDelList);
		} else {
			if (todoList.size() == 1 && LitigationDetailPojo.FROM.equals(todoList.get(0).getType())) {
				saveWithSerialCaseNumber(serialCaseNumber, from, parties, crawledData);
			}
			return;
		}

		if (!CollectionUtils.isEmpty(todoDelList)) {
			deleteHistory(todoDelList);
		}
		if (!isStop) {
			// 保存，
			saveWithSerialCaseNumber(serialCaseNumber, from, parties, crawledData);
		}

	}

	private boolean removeNotLastestUpdatetime(List<LitigationDetailPojo> todoList, LitigationParsedDataDetail from,
			List<LitigationParsedDataDetail> todoDel) {
		boolean isStop = false;
		// 按更新时间降序排序
		Collections.sort(todoList, new Comparator<LitigationDetailPojo>() {

			@Override
			public int compare(LitigationDetailPojo o1, LitigationDetailPojo o2) {
				Date d1 = o1.getDetail().getUpdatedDate();
				Date d2 = o2.getDetail().getUpdatedDate();
				if (d1.after(d2)) {
					return -1;
				} else if (d1.before(d2)) {
					return 1;
				} else {
					return 0;
				}
			}

		});

		// 取出更新时间最新的数据
		int i = 0;
		for (Iterator<LitigationDetailPojo> it = todoList.iterator(); it.hasNext();) {
			LitigationDetailPojo next = it.next();
			if (i == 0) {
				if (LitigationDetailPojo.FROM.equals(next.getType())) {
					isStop = false;
				}
			} else {
				if (LitigationDetailPojo.FROM.equals(next.getType())) {
					isStop = true;
				}
				if (LitigationDetailPojo.HISTORY.equals(next.getType())) {
					// 并删除原有库中数据
					todoDel.add(next.getDetail());
					it.remove();
				}

			}
			i++;
		}
		return isStop;
	}

	/**
	 * 根据自然人进行去重
	 * 
	 * @param todoList
	 * @param dataFrom(数据来源,0-optix,1-db2主表,2-db2进程表,3-sqlserver诉讼平台)
	 * @throws Exception
	 */
	private boolean removeParty(List<LitigationDetailPojo> todoList, LitigationParsedDataDetail from,
			List<LitigationParty> parties, LitigationCrawledData crawledData, List<LitigationParsedDataDetail> todoDel)
			throws Exception {
		boolean isStop = false;
		/**
		 * 如果是experian数据，选取sbdnum最多的诉讼 如果不是，选择原被告当事人最多的诉讼
		 */
		// todoList 踢出去from
		String idOfNew = from.getId();// 新数据id
		for (Iterator<LitigationDetailPojo> iter = todoList.iterator(); iter.hasNext();) {
			LitigationDetailPojo next = iter.next();
			LitigationParsedDataDetail detail = next.getDetail();
			if (LitigationDetailPojo.FROM.equals(next.getType()) && detail.getId().equals(idOfNew)) {
				iter.remove();
			}
		}
		List<LitigationParsedDataDetail> litis = new ArrayList<LitigationParsedDataDetail>();
		for (LitigationDetailPojo dp : todoList) {
			litis.add(dp.getDetail());
		}
		// experian数据
		int dataFrom = from.getDataFrom();
		if (dataFrom == 1 || dataFrom == 2 || dataFrom == 3) {
			// 库里有的数据的sbdnum个数
			List<Map<String, Object>> ordersMap = partyDao.batchSelectSbdnumDescOrder(litis);
			// 新来旧数据数据诉讼详情主键与sbdnum数映射
			Map<String, Integer> newDataMap = new HashMap<String, Integer>();
			if (!CollectionUtils.isEmpty(parties)) {
				int count = 0;
				for (LitigationParty p : parties) {
					String sbdnum = p.getSbdnum();
					if (StringUtils.isNotBlank(sbdnum)) {
						count++;
					}
				}
				newDataMap.put(from.getId(), count);
			}
			isStop = dataFilter(todoList, from, ordersMap, newDataMap, todoDel);
		} else {
			// 原被告当事人多的里面的第一个
			List<Map<String, Object>> ordersMap = partyDao.batchSelectAccPartyDescOrder(litis);
			// 新来旧数据数据诉讼详情主键与sbdnum数映射
			Map<String, Integer> newDataMap = new HashMap<String, Integer>();
			if (!CollectionUtils.isEmpty(parties)) {
				newDataMap.put(from.getId(), parties.size());
			}
			isStop = dataFilter(todoList, from, ordersMap, newDataMap, todoDel);
		}

		if (!isStop) {// 还要继续，需要把新来数据放回todoList里面
			todoList.add(new LitigationDetailPojo(from, LitigationDetailPojo.FROM));
		}

		return isStop;
	}

	/**
	 * 数据过滤，删除原有数据，新增新数据
	 * 
	 * @param todoList
	 *            原有数据
	 * @param from
	 *            新数据诉讼
	 * @param parties
	 *            新数据企业自然人
	 * @param ordersMap
	 *            排序map
	 * @throws Exception
	 */
	private boolean dataFilter(List<LitigationDetailPojo> todoList, LitigationParsedDataDetail from,
			List<Map<String, Object>> ordersMap, Map<String, Integer> newDataMap,
			List<LitigationParsedDataDetail> todoDel) {
		boolean isStop = false;
		try {
			for (Iterator<LitigationDetailPojo> it = todoList.iterator(); it.hasNext();) {
				LitigationDetailPojo pojo = it.next();
				LitigationParsedDataDetail detail = pojo.getDetail();
				int numOfOld = 0;
				for (Map<String, Object> m : ordersMap) {
					String detailId = m.get("parsed_data_detail_id").toString();
					if (detail.getId().equals(detailId)) {
						numOfOld = Integer.parseInt(m.get("num").toString());
					}
				}
				int numOfnew = 0;
				if (!CollectionUtils.isEmpty(newDataMap)) {
					numOfnew = newDataMap.get(from.getId()) == null ? 0 : newDataMap.get(from.getId());
				}
				if (numOfOld < numOfnew) {
					if (LitigationDetailPojo.HISTORY.equals(pojo.getType())) {
						todoDel.add(pojo.getDetail());
					}
					it.remove();
				} else {
					if (pojo.getDetail().getId().equals(from.getId())) {// 如果是新的
						isStop = true;// 停止检测了
					}
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}

		return isStop;

	}

	/**
	 * 按照案件类型
	 */
	private boolean removeCaseCategory(List<LitigationDetailPojo> todoList, List<LitigationParsedDataDetail> todoDel) {
		boolean isStop = false;
		// 判断案件类型是否都一样
		boolean allCaseSame = true;
		// 第一条案件类型
		int categoryIndex = CategorySort.indexof(todoList.get(0).getDetail().getCaseCategoryId());
		for (LitigationDetailPojo pojo : todoList) {
			// 有不同的
			if (CategorySort.indexof(pojo.getDetail().getCaseCategoryId()) != categoryIndex) {
				// 修改标识为不都相同
				allCaseSame = false;
			}
		}
		// 都相同返回进入下一环节
		if (allCaseSame) {
			return false;
		}
		// 都有案件编号
		boolean allYes = true;
		// 都无案件编号
		boolean allNo = true;
		for (LitigationDetailPojo pojo : todoList) {
			if (allYes && StringUtils.isEmpty(pojo.getDetail().getSimpleCaseNumber())) {
				allYes = false;
			} else if (allNo && !StringUtils.isEmpty(pojo.getDetail().getSimpleCaseNumber())) {
				allNo = false;
			}
		}
		// 都有或都无
		if (allYes || allNo) {
			Collections.sort(todoList);
			// 第一条案件类型
			categoryIndex = CategorySort.indexof(todoList.get(0).getDetail().getCaseCategoryId());
			// 移除优先级低的诉讼
			for (Iterator<LitigationDetailPojo> it = todoList.iterator(); it.hasNext();) {
				LitigationDetailPojo pojo = it.next();
				// 判断优先级，不在同一个优先级的，删掉
				if (CategorySort.indexof(pojo.getDetail().getCaseCategoryId()) != categoryIndex) {
					// todoList里面移除
					it.remove();

					if (LitigationDetailPojo.FROM.equals(pojo.getType())) {
						isStop = true;
					}

					if (LitigationDetailPojo.HISTORY.equals(pojo.getType())) {
						todoDel.add(pojo.getDetail());
					}
				}
			}
		}

		if (todoList.isEmpty()) {
			isStop = true;
		}
		return isStop;
	}

	/**
	 * 优先保留做过报告的(只针对旧数据)
	 * 
	 * @param todoList
	 */
	private boolean removeNoCourt(List<LitigationDetailPojo> todoList, List<LitigationParsedDataDetail> todoDel) {
		boolean isStop = false;
		/**
		 * 是否都没有做过报告 都没有不处理 不都是都没有，移除没做过报告的
		 */
		boolean allNoCourt = true;
		for (LitigationDetailPojo pojo : todoList) {
			if ("1".equals(pojo.getDetail().getIfBeCourt())) {
				allNoCourt = false;
			}
		}
		if (allNoCourt) {
			return false;
		}
		// 移除没有做过报告的
		for (Iterator<LitigationDetailPojo> it = todoList.iterator(); it.hasNext();) {
			LitigationDetailPojo pojo = it.next();
			if (!"1".equals(pojo.getDetail().getIfBeCourt())) {
				it.remove();
				if (LitigationDetailPojo.FROM.equals(pojo.getType())) {
					isStop = true;
				}

				if (LitigationDetailPojo.HISTORY.equals(pojo.getType())) {
					todoDel.add(pojo.getDetail());
				}
			}
		}

		if (todoList.isEmpty()) {
			isStop = true;
		}

		return isStop;
	}

	/**
	 * 优先保留有案号的
	 * 
	 * @param todoList
	 * @return 是否停止完备性检测：true为新来数据不具有完备性，结束完备性检测；false继续完备性检测
	 */
	private boolean removeNoCaseNumber(List<LitigationDetailPojo> todoList, List<LitigationParsedDataDetail> todoDel) {
		boolean isStop = false;
		/**
		 * 是否都没有案号 都没有不处理 不都是都没有案号，移除没有案号的
		 */
		// 都没有案号
		boolean allNoNum = true;
		for (LitigationDetailPojo pojo : todoList) {
			if (!StringUtils.isEmpty(pojo.getDetail().getSimpleCaseNumber())) {
				// 有跳出
				allNoNum = false;
				break;
			}
		}
		// 都没有案号直接返回
		if (allNoNum) {
			return false;
		}
		// 移除没有案号的
		for (Iterator<LitigationDetailPojo> it = todoList.iterator(); it.hasNext();) {
			LitigationDetailPojo pojo = it.next();
			if (StringUtils.isEmpty(pojo.getDetail().getSimpleCaseNumber())) {
				it.remove();
				// 新来数据的案号为空，原有数据有案号不为空，保留原有数据，停止其他完备性检测
				if (LitigationDetailPojo.FROM.equals(pojo.getType())) {
					isStop = true;
				}
				if (LitigationDetailPojo.HISTORY.equals(pojo.getType())) {
					todoDel.add(pojo.getDetail());
				}
			}
		}

		if (todoList.isEmpty()) {
			isStop = true;
		}

		return isStop;
	}

	/**
	 * 删除原有的，并将数据落到异常表中
	 * 
	 * @param history
	 */
	private void deleteHistory(List<LitigationParsedDataDetail> details) {
		if (CollectionUtils.isEmpty(details)) {
			return;
		}
		for (LitigationParsedDataDetail detail : details) {
			// 查看crawled_data,是否可以删除,此crawled_data没被解析成其他数据才能删除
			String crawledDataId = detail.getCrawledDataId();
			LitigationCrawledData crawledData = crawledDataDao.selectByPrimaryKey(crawledDataId);
			List<LitigationParty> parties = partyDao.selectByParsedDataDetailId(detail.getId());
			if (crawledDataHasNotBeenResoluted(crawledDataId)) {
				crawledDataDao.deleteByPrimaryKey(crawledDataId);
				logger.info("物理删抓取数据，主键id:" + crawledDataId);
			}
			parsedDataDetailDao.deleteByPrimaryKey(detail.getId());
			logger.info("物理删除诉讼数据，主键id:" + detail.getId());
			// 删除party
			partyDao.deleteByDetailId(detail.getId());
			logger.info("物理删除企业自认人数据，detailId:" + detail.getId());

			// 落入异常表中
			insertError(LitiBusiConstant.ErrorType.deplication.ordinal(), detail, crawledData, parties);
		}
	}

	/**
	 * 抓取数据表中数据是否没有被解析成详情数据
	 * 
	 * @param crawledDataId
	 * @return
	 */
	private boolean crawledDataHasNotBeenResoluted(String crawledDataId) {
		List<LitigationParsedDataDetail> details = parsedDataDetailDao.selectByCrawledDataId(crawledDataId);
		if (details == null)
			return true;
		return details.size() == 0;
	}

	/**
	 * 没有案号也没有上期案号
	 * 
	 * @param parsedDataDetail(诉讼案件)
	 * @param parties(自然人)
	 * @throws Exception
	 */
	private void noCaseNumberAndPreCaseNumber(LitigationParsedDataDetail parsedDataDetail,
			List<LitigationParty> parties, LitigationCrawledData crawledData) throws Exception {
		Integer courtId = parsedDataDetail.getCourtId();
		/**
		 * 有案号或上期案号，并且原被告当事人与来源数据的原被告当事人相同
		 * 
		 */
		List<LitigationParty> possibleParty = partyDao.selectByPartyList(parties, courtId, 1);
		/**
		 * 1、对可疑数据进行进一步筛选，主要是因为如果只是查询有案号或者上期案号的数据，数据量可能会有千万条。
		 * 所以先把自然人相关的查出来，在进行原被告当事人完全匹配，数据两会小很多。
		 */
		// 查找到了
		if (possibleParty != null && possibleParty.size() != 0) {
			// 按照诉讼详情id+案件审理时间进行分组
			Map<String, List<LitigationParty>> map = new HashMap<String, List<LitigationParty>>();
			for (LitigationParty p : possibleParty) {
				String mapKey = p.getParsedDataDetailId() + "_"
						+ (p.getAccuseDate() == null ? "" : p.getAccuseDate().getTime());
				// 没有存过
				if (map.get(mapKey) == null) {
					List<LitigationParty> lps = new ArrayList<LitigationParty>();
					lps.add(p);
					map.put(mapKey, lps);
				} else {
					map.get(mapKey).add(p);
				}
			}
			// 原被告是否相同
			Map<String, List<LitigationParty>> allSame = filterAccuserAccusedPartyAllSame(parties, map);
			if (allSame.size() > 0) {
				matchAccuseDate(parsedDataDetail, map, parties, crawledData);
			} else {
				noCaseNumberAndPreCaseNumberNext(parsedDataDetail, parties, crawledData);
			}
		} else {// 没有完全相同的
			noCaseNumberAndPreCaseNumberNext(parsedDataDetail, parties, crawledData);
		}
	}

	/**
	 * 按照案件审理时间进行查找,审理时间比对只能在用一个案件编号下
	 * 
	 * @param parsedDataDetail
	 * @param map
	 * @throws Exception
	 */
	private void matchAccuseDate(LitigationParsedDataDetail parsedDataDetail, Map<String, List<LitigationParty>> map,
			List<LitigationParty> parties, LitigationCrawledData crawledData) throws Exception {
		Iterator<Map.Entry<String, List<LitigationParty>>> it = map.entrySet().iterator();
		Set<String> tempSet = new HashSet<String>();
		String parsedDataId = null;
		String dateStrOfNew = parsedDataDetail.getAccuseDate() == null ? ""
				: parsedDataDetail.getAccuseDate().getTime() + "";
		while (it.hasNext()) {
			Map.Entry<String, List<LitigationParty>> entry = it.next();
			String detailId = entry.getKey();
			parsedDataId = parsedDataDetailDao.selectByPrimaryKey(detailId.substring(0, detailId.indexOf("_")))
					.getParsedDataId();
			tempSet.add(parsedDataId);
			/////////////////////////////////
			String dataStrOfOld = entry.getKey().substring(entry.getKey().indexOf("_") + 1);
			if (!dateStrOfNew.equals(dataStrOfOld)) {
				it.remove();
				// 继续下一个
				continue;
			}
		}
		// 先检测案件编号
		if (tempSet.size() > 1) {
			logger.error("************全部相同的原被告在不同的案件编号下，请检查，新数据id:" + parsedDataDetail.getId());
			return;
		}
		if (map.size() != 0) {
			List<String> detailIds = new ArrayList<String>();
			it = map.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, List<LitigationParty>> entry = it.next();
				detailIds.add(entry.getKey().substring(0, entry.getKey().indexOf("_")));
			}
			// 案件审理日期相同的数据
			List<LitigationParsedDataDetail> sameAccuseDateList = parsedDataDetailDao.selectByDetailIds(detailIds);
			// 找出案件状态相同的数据
			List<LitigationParsedDataDetail> sameAccuseStatusList = findSameAccuseStatusData(parsedDataDetail,
					sameAccuseDateList);
			if (!CollectionUtils.isEmpty(sameAccuseStatusList)) {
				// 按照完备性高的入库
				insertByBusiness(parsedDataDetail, sameAccuseStatusList, parties, crawledData);
			} else {
				// 新插入数据库，使用相同的案件编号
				saveWithSerialCaseNumber(parsedDataId, parsedDataDetail, parties, crawledData);
			}
		} else {
			// 新插入数据库，使用相同的案件编号
			saveWithSerialCaseNumber(parsedDataId, parsedDataDetail, parties, crawledData);
		}
	}

	/**
	 * 找出sameAccuseDateList数据中与parsedDataDetail案件状态相同的数据
	 * 
	 * @param parsedDataDetail
	 * @param sameAccuseDateList
	 * @return 案件相同的sameAccuseDateList
	 */
	private List<LitigationParsedDataDetail> findSameAccuseStatusData(LitigationParsedDataDetail parsedDataDetail,
			List<LitigationParsedDataDetail> sameAccuseDateList) {
		String accuseStatus = parsedDataDetail.getAccuseStatus();
		for (Iterator<LitigationParsedDataDetail> iter = sameAccuseDateList.iterator(); iter.hasNext();) {
			LitigationParsedDataDetail next = iter.next();
			String _status = next.getAccuseStatus();
			if (StringUtils.isNotEmpty(accuseStatus) && StringUtils.isNotEmpty(_status)
					&& !accuseStatus.equals(_status)) {
				iter.remove();
			}
		}
		return sameAccuseDateList;
	}

	/**
	 * 没有案号也没有上期案号 查找所有原被告当事人相同，并且没有案号也没有上期案号
	 * 
	 * @param parsedDataDetail
	 * @param parties
	 * @throws Exception
	 */
	private void noCaseNumberAndPreCaseNumberNext(LitigationParsedDataDetail parsedDataDetail,
			List<LitigationParty> parties, LitigationCrawledData crawledData) throws Exception {
		Integer courtId = parsedDataDetail.getCourtId();
		/**
		 * 有案号或上期案号，并且原被告当事人与来源数据的原被告当事人相同 与没有案号也没有上期案号进行匹配
		 */
		List<LitigationParty> possibleParty = partyDao.selectByPartyList(parties, courtId, 2);
		// 查找到了
		if (possibleParty != null && possibleParty.size() != 0) {
			Map<String, List<LitigationParty>> map = new HashMap<String, List<LitigationParty>>();
			for (LitigationParty p : possibleParty) {
				String mapKey = p.getParsedDataDetailId() + "_"
						+ (p.getAccuseDate() == null ? "" : p.getAccuseDate().getTime());
				// 没有存过
				if (map.get(mapKey) == null) {
					List<LitigationParty> lps = new ArrayList<LitigationParty>();
					lps.add(p);
					map.put(mapKey, lps);
				} else {
					map.get(mapKey).add(p);
				}
			}
			Map<String, List<LitigationParty>> allSame = filterAccuserAccusedPartyAllSame(parties, map);
			if (allSame.size() > 0) {// 有原被告当时人全部相同
				matchAccuseDate(parsedDataDetail, allSame, parties, crawledData);
			} else {// 有原被告当时人部分相同或者全部不相同
					// 新插入(使用新的案件编号)
				save(parsedDataDetail, parties, crawledData, null);
				return;
			}
		} else {// 没有找到 新插入(使用新的案件编号)
			save(parsedDataDetail, parties, crawledData, null);
		}
	}

	/**
	 * 过滤出原有数据与新来数据原被告当事人全部相同的
	 * 
	 * @param parties
	 *            新来的
	 * @param map
	 *            原来的 按parsed_data_id分类的party
	 * @return
	 */
	private Map<String, List<LitigationParty>> filterAccuserAccusedPartyAllSame(List<LitigationParty> parties,
			Map<String, List<LitigationParty>> map) {
		Map<String, List<LitigationParty>> allSameMap = new HashMap<String, List<LitigationParty>>();
		Iterator<Map.Entry<String, List<LitigationParty>>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, List<LitigationParty>> entry = it.next();
			List<LitigationParty> partyByDetailList = entry.getValue();
			if (parties.size() != partyByDetailList.size()) {
				it.remove();
				// 继续下一个
				continue;
			}
			// 分别看原告、被告、当事人是否全部相同
			List<String> accusers = new ArrayList<String>();// 原告
			List<String> accuseds = new ArrayList<String>();// 被告
			List<String> partys = new ArrayList<String>();// 当事人
			for (LitigationParty p : partyByDetailList) {
				Integer partyCategoryId = p.getPartyCategoryId();
				String name = p.getName();
				if (StringUtils.isNotBlank(name)) {
					if (LitiBusiConstant.PartyCategory.ACCUSER == partyCategoryId) {
						accusers.add(name);
					} else if (LitiBusiConstant.PartyCategory.ACCUSED == partyCategoryId) {
						accuseds.add(name);
					} else if (LitiBusiConstant.PartyCategory.PARTIES == partyCategoryId) {
						partys.add(name);
					}
				}
			}

			List<String> accusersOfOrigin = new ArrayList<String>();
			List<String> accusedsOfOrigin = new ArrayList<String>();
			List<String> partissOfOrigin = new ArrayList<String>();
			for (LitigationParty pn : parties) {
				String name = pn.getName();
				if (StringUtils.isNotBlank(name)) {
					if (LitiBusiConstant.PartyCategory.ACCUSER == pn.getPartyCategoryId()) {// 原告
						accusersOfOrigin.add(name);
					} else if (LitiBusiConstant.PartyCategory.ACCUSED == pn.getPartyCategoryId()) {// 被告
						accusedsOfOrigin.add(name);
					} else if (LitiBusiConstant.PartyCategory.PARTIES == pn.getPartyCategoryId()) {// 当事人
						partissOfOrigin.add(name);
					}
				}
			}

			if (isSame(accusersOfOrigin, accusers) && isSame(accusedsOfOrigin, accuseds)
					&& isSame(partissOfOrigin, partys)) {
				allSameMap.put(entry.getKey(), entry.getValue());
			}
		}
		return allSameMap;
	}

	private static boolean isSame(List<String> newData, List<String> oldData) {
		int newDataSize = newData.size();
		int oldDataSize = oldData.size();
		if (newDataSize == 0 && newDataSize == oldDataSize) {
			return true;
		}
		if (newDataSize != oldDataSize) {
			return false;
		}
		Collections.sort(newData);
		Collections.sort(oldData);
		for (int i = 0; i < newData.size(); i++) {
			if (!newData.get(i).equals(oldData.get(i)))
				return false;
		}
		return true;
	}

	public static class SerialCaseNumber implements Comparable<SerialCaseNumber> {
		private String value;

		public SerialCaseNumber(String value) {
			this.value = value;
		}

		@Override
		public int compareTo(SerialCaseNumber o) {
			long valueOfThis = 0;
			if (value.startsWith(LitiBusiConstant.Prefix.EXPERIAL) || value.startsWith(LitiBusiConstant.Prefix.OPTIX)) {
				valueOfThis = Long.valueOf(value.substring(1));
			} else {
				valueOfThis = Long.valueOf(value);
			}
			long valueOfThat = 0;
			if (o.value.startsWith(LitiBusiConstant.Prefix.EXPERIAL)
					|| o.value.startsWith(LitiBusiConstant.Prefix.OPTIX)) {
				valueOfThat = Long.valueOf(o.value.substring(1));
			} else {
				valueOfThat = Long.valueOf(o.value);
			}
			if (valueOfThis > valueOfThat) {
				return 1;
			} else if (valueOfThis < valueOfThat) {
				return -1;
			} else {
				return 0;
			}
		}
	}

	@Override
	public List<LitigationAPIPojo> getLitigationAPIPojo(int pageNum, int pageSize, final Integer fetchState,
			final Integer parsedStatus) {
		List<LitigationAPIPojo> pojos = new ArrayList<LitigationAPIPojo>();
		Page<LitigationTempParsedDataDetail> page = PageHelper.startPage(pageNum, pageSize).doSelectPage(new ISelect() {

			@Override
			public void doSelect() {
				tempParsedDataDetailDao.selectUnFetchData(fetchState, parsedStatus);
			}
		});
		List<LitigationTempParsedDataDetail> tempPdds = page.getResult();
		for (LitigationTempParsedDataDetail tempPdd : tempPdds) {
			LitigationAPIPojo pojo = detailsAdapter(tempPdd);
			pojos.add(pojo);

		}
		return pojos;
	}

	@Override
	public List<LitigationAPIPojo> getLastest(long num, Integer fetchState, Integer parsedStatus) {
		List<LitigationTempParsedDataDetail> tempDetails = tempParsedDataDetailDao.selectLastest(num, fetchState,
				parsedStatus);
		if (CollectionUtils.isEmpty(tempDetails))
			return null;
		List<LitigationAPIPojo> pojos = new ArrayList<LitigationAPIPojo>();
		for (LitigationTempParsedDataDetail tempDetail : tempDetails) {
			LitigationAPIPojo pojo = detailsAdapter(tempDetail);
			pojos.add(pojo);
		}
		return pojos;
	}

	@Override
	public LitigationAPIPojo getAPiPojoById(String detailId, Integer fetchState, Integer parsedStatus) {
		LitigationTempParsedDataDetail detail = tempParsedDataDetailDao.selectUnFetchDataByPrimaryKey(detailId,
				fetchState, parsedStatus);
		if (detail == null) {
			return null;
		}
		return detailsAdapter(detail);
	}

	/**
	 * 数据适配器
	 * 
	 * @param tempDetail
	 * @return
	 */
	private LitigationAPIPojo detailsAdapter(LitigationTempParsedDataDetail tempDetail) {
		String detailId = tempDetail.getId();
		String crawledDataId = tempDetail.getCrawledDataId();
		List<LitigationTempParty> tempPs = tempPartyDao.selectByDetailId(detailId);
		LitigationTempCrawledData tempCrawleds = tempCrawledDataDao.selectByPrimaryKey(crawledDataId);

		LitigationParsedDataDetail detail = gson.fromJson(gson.toJson(tempDetail), LitigationParsedDataDetail.class);
		List<LitigationParty> partis = gson.fromJson(gson.toJson(tempPs), new TypeToken<List<LitigationParty>>() {
		}.getType());
		LitigationCrawledData party = gson.fromJson(gson.toJson(tempCrawleds), LitigationCrawledData.class);
		LitigationAPIPojo liti = new LitigationAPIPojo();
		liti.setLitigationParsedDataDetail(detail);
		liti.setLitigationPartyList(partis);
		liti.setLitigationCrawledData(party);
		return liti;
	}

	@Override
	public void oldDataDeplidate(final Integer fetchState, final Integer parsedStatus) {
		int countNum = tempParsedDataDetailDao.getOldDataCount(fetchState, parsedStatus);
		logger.info("旧记录总数：" + countNum);
		if (countNum > 0) {
			/**
			 * 每个线程跑10000条数据
			 */
			// 页码
			int curPage = 0;
			// 每页数量
			int pageSize = 1000;
			while (curPage * pageSize < countNum) {
				curPage++;
				Page<LitigationTempParsedDataDetail> page = PageHelper.startPage(curPage, pageSize)
						.doSelectPage(new ISelect() {

							@Override
							public void doSelect() {
								tempParsedDataDetailDao.selectUnFetchData(fetchState, parsedStatus);
							}
						});
				List<LitigationTempParsedDataDetail> tempPdds = page.getResult();
				if (!CollectionUtils.isEmpty(tempPdds)) {
					for (LitigationTempParsedDataDetail pd : tempPdds) {
						LitigationAPIPojo pojo = detailsAdapter(pd);
						tempParsedDataDetailDao.updateFetchFlagByPrimaryKey(
								pojo.getLitigationParsedDataDetail().getId(), LitiBusiConstant.DataFetchStatus.FETCHED);
						if (pojo.getLitigationParsedDataDetail().getCourtId() == null) {
							continue;
						}
						int courtId = pojo.getLitigationParsedDataDetail().getCourtId();
						int mod = courtId % ConstantConfig.Queue.QUEUE_SIZE;
						String queueName = ConstantConfig.Queue.QUEUE_NAME_PREFIX + mod;
						logger.info("id:" + pojo.getLitigationParsedDataDetail().getId() + ",courtId:" + courtId
								+ ",入队列:" + queueName);
						rabbit.sendMsg(queueName, gson.toJson(pojo));
					}
				}
			}
		}
	}
}
