package com.experian.daas.litigation.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.experian.comp.rabbitmq.RabbitClient;
import com.experian.core.constant.LitiBusiConstant;
import com.experian.core.pojo.ApiResult;
import com.experian.core.pojo.ApiResult.Errors;
import com.experian.core.pojo.R;
import com.experian.daas.litigation.config.ConstantConfig;
import com.experian.daas.litigation.dao.LitigationCaseCategoryDao;
import com.experian.daas.litigation.dao.LitigationCourtDao;
import com.experian.daas.litigation.dao.LitigationPartyCategoryDao;
import com.experian.daas.litigation.dao.LitigationPartyDao;
import com.experian.daas.litigation.dao.LitigationSourceUrlDao;
import com.experian.daas.litigation.dao.LitigationTempCrawledDataDao;
import com.experian.daas.litigation.dao.LitigationTempParsedDataDetailDao;
import com.experian.daas.litigation.dao.LitigationTempPartyDao;
import com.experian.daas.litigation.service.LitigationChannelService;
import com.experian.daas.litigation.utility.CaseNumberTransfer;
import com.experian.daas.litigation.utility.PKUtil;
import com.experian.dto.entity.litigation.LitigationCaseCategory;
import com.experian.dto.entity.litigation.LitigationCourt;
import com.experian.dto.entity.litigation.LitigationCrawledData;
import com.experian.dto.entity.litigation.LitigationParsedDataDetail;
import com.experian.dto.entity.litigation.LitigationParty;
import com.experian.dto.entity.litigation.LitigationPartyCategory;
import com.experian.dto.entity.litigation.LitigationSourceUrl;
import com.experian.dto.entity.litigation.LitigationTempCrawledData;
import com.experian.dto.entity.litigation.LitigationTempParsedDataDetail;
import com.experian.dto.entity.litigation.LitigationTempParty;
import com.experian.dto.pojo.litigation.LitigationAPIPojo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service("litigationChannelService")
@Transactional(rollbackFor = Exception.class)
public class LitigationChannelServiceImpl implements LitigationChannelService {
	private static final Logger logger = Logger.getLogger(LitigationChannelService.class);

	@Resource
	private LitigationCaseCategoryDao litigationCaseCategoryDao;
	@Resource
	private LitigationCourtDao litigationCourtDao;
	@Resource
	private LitigationSourceUrlDao litigationSourceUrlDao;
	@Resource
	private LitigationPartyDao litigationPartyDao;

	@Resource
	private LitigationTempCrawledDataDao tempCrawledDataDao;

	@Resource
	private LitigationTempParsedDataDetailDao tempParsedDataDetailDao;

	@Resource
	private LitigationTempPartyDao tempPartyDao;

	@Resource
	private LitigationPartyCategoryDao litigationPartyCategoryDao;

	@Autowired
	private RabbitClient rabbit;

	private Gson gson = new Gson();

	@Override
	public ApiResult<Void> litigationNewCaseCategory(List<LitigationCaseCategory> caseCategories) {
		ApiResult<Void> r = new ApiResult<Void>();
		List<Errors> errors = new ArrayList<Errors>();
		if (CollectionUtils.isEmpty(caseCategories)) {
			errors.add(new Errors(Errors.Type.OTHER, null, "没有数据"));
			r.setError(errors);
			r.setStatus(R.FAILED);
			return r;
		}
		for (LitigationCaseCategory cc : caseCategories) {
			try {
				if (litigationCaseCategoryDao.selectByPrimaryKey(cc.getId()) == null) {
					litigationCaseCategoryDao.insert(cc);
				} else {
					errors.add(new Errors(Errors.Type.DUPLICATE_KEY, cc.getId() + "", "主键重复"));
				}
			} catch (Exception e) {
				logger.error(e);
				errors.add(new Errors(Errors.Type.OTHER, cc.getId() + "", e.getMessage()));
			}
		}
		if (errors.isEmpty()) {
			return new ApiResult<Void>();
		}
		r.setError(errors);
		r.setStatus(R.FAILED);
		return r;
	}

	@Override
	public ApiResult<Void> litigationNewCourt(List<LitigationCourt> courts) {
		ApiResult<Void> r = new ApiResult<Void>();
		List<Errors> errors = new ArrayList<Errors>();
		if (CollectionUtils.isEmpty(courts)) {
			errors.add(new Errors(Errors.Type.OTHER, null, "没有数据"));
			r.setError(errors);
			r.setStatus(R.FAILED);
			return r;
		}
		logger.info("<<<<<<<<法院自然人字典表API入库开始：>>>>>>>>>>本批数据量:" + courts.size());
		for (LitigationCourt c : courts) {
			try {
				if (litigationCourtDao.selectByPrimaryKey(c.getId()) == null) {
					litigationCourtDao.insert(c);
					logger.info("<<<<<<<<法院自然人入库：>>>>>>>>>>id:" + c.getId());
				} else {
					errors.add(new Errors(Errors.Type.DUPLICATE_KEY, c.getId() + "", "主键重复"));
					logger.info("<<<<<<<<法院字段表 API入库开始>>>>>>>>>>id:" + c.getId());
				}
			} catch (Exception e) {
				logger.error(e);
				errors.add(new Errors(Errors.Type.OTHER, c.getId() + "", e.getMessage()));
			}
		}
		if (errors.isEmpty()) {
			return new ApiResult<Void>();
		}
		r.setError(errors);
		r.setStatus(R.FAILED);
		logger.info("<<<<<<<<法院自然人字典表API入库结束：>>>>>>>>>>本批数据量:" + courts.size());
		return r;
	}

	/**
	 * optix数据落入临时表中
	 */
	@Override
	public ApiResult<Void> litigationChannel(List<LitigationAPIPojo> litigations) {
		ApiResult<Void> r = new ApiResult<Void>();
		List<Errors> errors = new ArrayList<Errors>();
		String tempDetailId = null;
		if (CollectionUtils.isEmpty(litigations)) {
			errors.add(new Errors(Errors.Type.OTHER, null, "没有数据"));
			r.setError(errors);
			r.setStatus(R.FAILED);
			return r;
		}
		logger.info("<<<<<<<<诉讼主体数据API入库开始>>>>>>>>>>本批数据量:" + litigations.size());
		int count = 0;
		for (LitigationAPIPojo litigation : litigations) {
			try {
				LitigationParsedDataDetail litigationParsedDataDetail = litigation.getLitigationParsedDataDetail();
				LitigationCrawledData litigationCrawledData = litigation.getLitigationCrawledData();
				List<LitigationParty> litigationPartyList = litigation.getLitigationPartyList();
				if (!newDataValidate(litigationParsedDataDetail, litigationCrawledData, litigationPartyList, errors)) {
					logger.info("<<诉讼主体API数据校验异常:>>" + gson.toJson(errors));
					continue;
				}
				// 检查供应商推送数据是否已经推送过
				String detailSupplierId = litigationParsedDataDetail.getId();
				List<LitigationTempParsedDataDetail> existDetail = tempParsedDataDetailDao
						.selectBySupplierId(detailSupplierId);
				if (!CollectionUtils.isEmpty(existDetail)) {// 已经推送过
					errors.add(new Errors(Errors.Type.DUPLICATE_KEY, detailSupplierId, "已经推送"));
					logger.error(String.format("供应商推送数据id[%s]已经存在", detailSupplierId));
					continue;
				}
				String tempCrawledDataId = PKUtil.optixKey();
				;
				String tempCrawledDaraSupplierId = litigationCrawledData.getId();
				logger.info("<<<<<<<<抓取数据准备入库：>>>>>>>>>>id:" + tempCrawledDaraSupplierId);
				List<LitigationTempCrawledData> tcds = tempCrawledDataDao.selectBySupplierId(tempCrawledDaraSupplierId);
				LitigationTempCrawledData litigationTempCrawledData = null;
				if (CollectionUtils.isEmpty(tcds)) {// 一对多关系，没有才插入
					litigationTempCrawledData = new LitigationTempCrawledData();
					BeanUtils.copyProperties(litigationCrawledData, litigationTempCrawledData);
					litigationTempCrawledData.setId(tempCrawledDataId);
					litigationTempCrawledData.setSupplierId(tempCrawledDaraSupplierId);
					litigationTempCrawledData.setInsertDate(new Date());
					tempCrawledDataDao.insert(litigationTempCrawledData);
					logger.info("<<<<<<<<抓取数据入库成功：>>>>>>>>>>supplierId:" + tempCrawledDaraSupplierId + ",id:"
							+ tempCrawledDataId);
				} else {
					tempCrawledDataId = tcds.get(0).getId();
					litigationTempCrawledData = tcds.get(0);
					logger.info("<<<<<<<<已有抓取数据：>>>>>>>>>>supplierId:" + tempCrawledDaraSupplierId + ",id:"
							+ tempCrawledDataId);
				}

				// 落入诉讼主表（详情表）
				LitigationTempParsedDataDetail litigationTempParsedDataDetail = new LitigationTempParsedDataDetail();
				BeanUtils.copyProperties(litigationParsedDataDetail, litigationTempParsedDataDetail);
				tempDetailId = PKUtil.optixKey();
				Date accuseDate = litigationParsedDataDetail.getAccuseDate();
				String tempParsedDataId = litigationParsedDataDetail.getParsedDataId();
				litigationTempParsedDataDetail.setId(tempDetailId);
				litigationTempParsedDataDetail.setSupplierId(detailSupplierId);
				litigationTempParsedDataDetail.setParsedDataId(tempParsedDataId);
				litigationTempParsedDataDetail.setCrawledDataId(tempCrawledDataId);
				// 简单案号
				litigationTempParsedDataDetail
						.setSimpleCaseNumber(CaseNumberTransfer.toSimple(litigationParsedDataDetail.getCaseNumber()));
				// 简单上期案号
				litigationTempParsedDataDetail.setSimplePreviousCaseNumber(
						CaseNumberTransfer.toSimple(litigationParsedDataDetail.getPreviousCaseNumber()));
				litigationTempParsedDataDetail.setDataFrom(LitiBusiConstant.DataFrom.OPTIX);
				litigationTempParsedDataDetail.setInsertDate(new Date());
				litigationTempParsedDataDetail.setFetchFlag(LitiBusiConstant.DataFetchStatus.UN_FETCH);
				logger.info("<<<<<<<<详情数据准备入库：>>>>>>>>>>id:" + detailSupplierId);
				tempParsedDataDetailDao.insert(litigationTempParsedDataDetail);
				logger.info("<<<<<<<<详情数据入库成功：>>>>>>>>>>supplierId:" + detailSupplierId + ",id:" + tempDetailId
						+ ",crawledDataId:" + tempCrawledDataId);
				count++;

				// 批量落入企业自然人表
				List<LitigationTempParty> litigationTempPartyList = new ArrayList<LitigationTempParty>();
				String partyGeneIdStr = "";
				if (!CollectionUtils.isEmpty(litigationPartyList)) {
					String fromIdStr = "";
					for (LitigationParty p : litigationPartyList) {
						LitigationTempParty litigationTempParty = new LitigationTempParty();
						BeanUtils.copyProperties(p, litigationTempParty);
						String partyId = p.getId();
						String partyGeneId = PKUtil.optixKey();
						litigationTempParty.setId(partyGeneId);
						litigationTempParty.setSupplierId(partyId);
						litigationTempParty.setParsedDataDetailId(tempDetailId);
						litigationTempParty.setInsertDate(new Date());
						litigationTempParty.setAccuseDate(accuseDate);// 冗余一个案件审理时间
						litigationTempPartyList.add(litigationTempParty);
						fromIdStr += partyId + ",";
						partyGeneIdStr += partyGeneId + ",";
					}
					logger.info("<<<<<<<<企业自然人数据准备入库：>>>>>>>>>>size:" + litigationPartyList.size() + ",ids:"
							+ fromIdStr.substring(0, fromIdStr.length() - 1));
				}
				if (!CollectionUtils.isEmpty(litigationTempPartyList)) {
					tempPartyDao.batchInset(litigationTempPartyList);
					logger.info("<<<<<<<<企业自然人数据入库成功：>>>>>>>>>>size:" + litigationTempPartyList.size() + ",our_ids:"
							+ partyGeneIdStr.substring(0, partyGeneIdStr.length() - 1) + ",detailId:" + tempDetailId);
				} else {
					logger.info("<<<<<<<<没有企业自然人>>>>>>>>>>detailId:" + tempDetailId);
				}
				// 丢进消息队列执行去重
				Integer _courtId = litigationParsedDataDetail.getCourtId();
				if (_courtId == null) {
					logger.error(
							String.format("数据[%s]-[%s]courtId为空，不执行去重", tempCrawledDataId, tempCrawledDaraSupplierId));
				} else {
					int courtId = _courtId.intValue();
					int mod = courtId % ConstantConfig.Queue.QUEUE_SIZE;
					String queueName = ConstantConfig.Queue.QUEUE_NAME_PREFIX + mod;
					logger.info(String.format("id:%s,courtId:%s,入队列:%s", tempCrawledDataId, courtId, queueName));
					LitigationAPIPojo pojo = temp2Pojo(litigationTempCrawledData, litigationTempParsedDataDetail,
							litigationTempPartyList);
					rabbit.sendMsg(queueName, gson.toJson(pojo));
				}
			} catch (Exception e) {
				logger.error(e);
				errors.add(new Errors(Errors.Type.OTHER, tempDetailId, e.getMessage()));
			}
		}
		logger.info("<<<<<<<<诉讼主体数据API入库结束：>>>>>>>>>>本批数据入库量:" + count);
		if (errors.isEmpty()) {
			return new ApiResult<Void>();
		}
		r.setError(errors);
		r.setStatus(R.FAILED);
		return r;
	}

	/**
	 * temp数据转到pojo
	 * @param litigationTempCrawledData
	 * @param litigationTempParsedDataDetail
	 * @param litigationTempPartyList
	 * @return
	 */
	private LitigationAPIPojo temp2Pojo(LitigationTempCrawledData litigationTempCrawledData,
			LitigationTempParsedDataDetail litigationTempParsedDataDetail,
			List<LitigationTempParty> litigationTempPartyList) {
		LitigationAPIPojo pojo = new LitigationAPIPojo();
		LitigationCrawledData crawledData = new LitigationCrawledData();
		BeanUtils.copyProperties(litigationTempCrawledData, crawledData);
		LitigationParsedDataDetail detail = new LitigationParsedDataDetail();
		BeanUtils.copyProperties(litigationTempParsedDataDetail, detail);
		List<LitigationParty> parties = gson.fromJson(gson.toJson(litigationTempPartyList),
				new TypeToken<List<LitigationParty>>() {
				}.getType());
		pojo.setLitigationCrawledData(crawledData);
		pojo.setLitigationParsedDataDetail(detail);
		pojo.setLitigationPartyList(parties);
		return pojo;
	}

	/**
	 * 诉讼主体数据校验
	 * 
	 * @param litigationParsedDataDetail
	 * @param litigationCrawledData
	 * @param litigationPartyList
	 * @param errors
	 *            异常信息
	 * @return true 合格 false 不合格
	 */
	private boolean newDataValidate(LitigationParsedDataDetail litigationParsedDataDetail,
			LitigationCrawledData litigationCrawledData, List<LitigationParty> litigationPartyList,
			List<Errors> errors) {
		/**
		 * 我们不接受 逻辑不正确的数据，我们只接收如下正确的数据：<br>
		 * 1. 成功拆分数据 status=0 且 各表必须有相关联的数据 <br>
		 * 2. 包含剔除词无用的数据 status=1 ，这种数据至少是与URL 和抓取数据表 有关联的数据 <br>
		 * 3. 有用但未成功拆分数据 status=2，这种数据至少是与URL 和抓取数据表 有关联的数据<br>
		 * 4. 判重重复数据 status=3、4且 各表必须有相关联的数据<br>
		 * 5. 人工标识无用的数据 Status=5且 各表必须有相关联的数据
		 */
		String detailId = litigationParsedDataDetail.getId();
		Boolean deduplicated = litigationParsedDataDetail.getDeduplicated();
		Integer status = litigationParsedDataDetail.getStatus();
		if (deduplicated == null) {
			errors.add(new Errors(Errors.Type.OTHER, detailId, "deduplicated为空"));
			return false;
		}
		if (deduplicated == false) {
			errors.add(new Errors(Errors.Type.OTHER, detailId, "deduplicated为false"));
			return false;
		}
		if (status == null) {
			errors.add(new Errors(Errors.Type.OTHER, detailId, "status为空"));
			return false;
		}
		String parsedDataId = litigationParsedDataDetail.getParsedDataId();
		if ("0".equals(status) || "5".equals(status) || "3".equals(status) || "4".equals(status)) {
			if (litigationCrawledData == null) {
				errors.add(new Errors(Errors.Type.OTHER, detailId, "status为" + status + "，但抓取数据为空"));
				return false;
			}
			if (CollectionUtils.isEmpty(litigationPartyList)) {
				errors.add(new Errors(Errors.Type.OTHER, detailId, "status为" + status + "，但企业自人数据为空"));
				return false;
			}
			if (StringUtils.isEmpty(parsedDataId)) {
				errors.add(new Errors(Errors.Type.OTHER, detailId, "status为" + status + "，但案件编号为空"));
				return false;
			}
			if (!litigationCrawledData.getId().equals(litigationParsedDataDetail.getCrawledDataId())) {
				errors.add(new Errors(Errors.Type.OTHER, detailId,
						"status为" + status + "，但抓取数据id[" + litigationCrawledData.getId() + "]与详情crawledDataId["
								+ litigationParsedDataDetail.getCrawledDataId() + "]不关联"));
				return false;
			}
			if (litigationPartyList.size() > 0) {
				for (LitigationParty p : litigationPartyList) {
					if (!detailId.equals(p.getParsedDataDetailId())) {
						errors.add(new Errors(Errors.Type.OTHER, detailId, "status为" + status + "，但详情id[" + detailId
								+ "]与企业自然人(" + p.getId() + ")parsedDetailId[" + p.getParsedDataDetailId() + "]不关联"));
						return false;
					}
				}
			}
		}
		if ("1".equals(status) || "2".equals(status)) {
			if (litigationCrawledData == null) {
				errors.add(new Errors(Errors.Type.OTHER, detailId, "status为" + status + "，但抓取数据为空"));
				return false;
			}
			if (!litigationCrawledData.getId().equals(litigationParsedDataDetail.getCrawledDataId())) {
				errors.add(new Errors(Errors.Type.OTHER, detailId,
						"status为" + status + "，但抓取数据id[" + litigationCrawledData.getId() + "]与详情crawledDataId["
								+ litigationParsedDataDetail.getCrawledDataId() + "]不关联"));
				return false;
			}
		}
		return true;
	}

	@Override
	public ApiResult<Void> litigationNewSourceUrl(List<LitigationSourceUrl> urls) {
		ApiResult<Void> r = new ApiResult<Void>();
		List<Errors> errors = new ArrayList<Errors>();
		if (CollectionUtils.isEmpty(urls)) {
			errors.add(new Errors(Errors.Type.OTHER, null, "没有数据"));
			r.setError(errors);
			r.setStatus(R.FAILED);
			return r;
		}
		logger.info("<<<<<<<<URL字段表 API入库开始：>>>>>>>>>>本批数据量:" + urls.size());
		for (LitigationSourceUrl su : urls) {
			try {
				if (litigationSourceUrlDao.selectByPrimaryKey(su.getId()) == null) {
					litigationSourceUrlDao.insert(su);
					logger.info("<<<<<<<<URL字段表入库>>>>>>>>>>id:" + su.getId());
				} else {
					errors.add(new Errors(Errors.Type.DUPLICATE_KEY, su.getId() + "", "主键重复"));
					logger.info("URL字典表入库主键重复:" + su.getId());
				}
			} catch (Exception e) {
				logger.error(e);
				errors.add(new Errors(Errors.Type.OTHER, su.getId() + "", e.getMessage()));
			}
		}
		if (errors.isEmpty()) {
			return new ApiResult<Void>();
		}
		r.setError(errors);
		r.setStatus(R.FAILED);
		logger.info("<<<<<<<<URL字段表 API入库结束：>>>>>>>>>>本批数据量:" + urls.size());
		return r;
	}

	@Override
	public ApiResult<Void> litigationNewPartyCategory(List<LitigationPartyCategory> partyCategories) {
		ApiResult<Void> r = new ApiResult<Void>();
		List<Errors> errors = new ArrayList<Errors>();
		if (CollectionUtils.isEmpty(partyCategories)) {
			errors.add(new Errors(Errors.Type.OTHER, null, "没有数据"));
			r.setError(errors);
			r.setStatus(R.FAILED);
			return r;
		}
		for (LitigationPartyCategory pc : partyCategories) {
			try {
				if (litigationPartyCategoryDao.selectByPrimaryKey(pc.getId()) == null) {
					litigationPartyCategoryDao.insert(pc);
				} else {
					errors.add(new Errors(Errors.Type.DUPLICATE_KEY, pc.getId() + "", "主键重复"));
				}
			} catch (Exception e) {
				logger.error(e);
				errors.add(new Errors(Errors.Type.OTHER, pc.getId() + "", e.getMessage()));
			}
		}
		if (errors.isEmpty()) {
			return new ApiResult<Void>();
		}
		r.setError(errors);
		r.setStatus(R.FAILED);
		return r;
	}

}
