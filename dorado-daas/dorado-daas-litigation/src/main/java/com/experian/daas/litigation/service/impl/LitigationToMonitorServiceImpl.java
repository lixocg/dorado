package com.experian.daas.litigation.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.experian.comp.rabbitmq.RabbitClient;
import com.experian.comp.utility.RedisUtil;
import com.experian.core.enums.Srv;
import com.experian.core.rest.RestClient;
import com.experian.daas.litigation.config.ConstantConfig;
import com.experian.daas.litigation.dao.LitigationCaseCategoryDao;
import com.experian.daas.litigation.dao.LitigationCourtDao;
import com.experian.daas.litigation.dao.LitigationCrawledDataDao;
import com.experian.daas.litigation.dao.LitigationParsedDataDetailDao;
import com.experian.daas.litigation.dao.LitigationPartyDao;
import com.experian.daas.litigation.dao.LitigationSourceUrlDao;
import com.experian.daas.litigation.service.LitigationToMonitorService;
import com.experian.dto.entity.litigation.LitigationCaseCategory;
import com.experian.dto.entity.litigation.LitigationCourt;
import com.experian.dto.entity.litigation.LitigationCrawledData;
import com.experian.dto.entity.litigation.LitigationParsedDataDetail;
import com.experian.dto.entity.litigation.LitigationParty;
import com.experian.dto.entity.litigation.LitigationSourceUrl;
import com.experian.dto.pojo.litigation.Litigation;
import com.experian.dto.pojo.litigation.LitigationAPIPojo;
import com.github.pagehelper.StringUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service
public class LitigationToMonitorServiceImpl implements LitigationToMonitorService {
	private static final Logger logger = Logger.getLogger(LitigationAdapterServiceImpl.class);

	@Resource
	private LitigationParsedDataDetailDao parsedDataDetailDao;

	@Resource
	private LitigationPartyDao partyDao;

	@Resource
	private LitigationCrawledDataDao crawledDataDao;

	@Resource
	private LitigationCaseCategoryDao caseCategoryDao;

	@Resource
	private LitigationCourtDao courtDao;

	@Resource
	private LitigationSourceUrlDao sourceUrlDao;

	@Autowired
	private RabbitClient rabbit;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public String transLitigationTo(LitigationAPIPojo litigation) {
		logger.info(String.format("进入消息队列时间开始", ""));
		long s = System.currentTimeMillis();
		LitigationParsedDataDetail detail = litigation.getLitigationParsedDataDetail();
		LitigationCrawledData crawledData = litigation.getLitigationCrawledData();
		List<LitigationParty> parties = litigation.getLitigationPartyList();

		logger.info(String.format("1.获取对象：%s", (System.currentTimeMillis()-s)));
		// 企业自然人匹配sbdnum
		for (LitigationParty p : parties) {
			String name = p.getName();
			if (StringUtils.isNotBlank(p.getSbdnum())) {
				p.setSbdnum(p.getSbdnum());
			} else {
				if (StringUtils.isNotBlank(name)) {
					// 先从redis中获取
					String sbdnum = RedisUtil.get(name);
					if (StringUtils.isEmpty(sbdnum)) {// redis中没有请求基本信息服务获取sbdnum
						long s2 = System.currentTimeMillis();
						sbdnum = RestClient.get(Srv.baseinfo, "/baseinfo/match/{name}", String.class, name);
						logger.info(String.format("%s-请求sbdnum:%s",name, System.currentTimeMillis()-s2));
					}
					if (StringUtil.isNotEmpty(sbdnum)) {
						// 放入redis中
						RedisUtil.set(name, sbdnum);
						p.setSbdnum(sbdnum);
					}
				}
			}
		}
		logger.info(String.format("获取sbdnum:%s", (System.currentTimeMillis()-s)));

		Litigation li = new Litigation();
		Integer caseCategoryId = detail.getCaseCategoryId();
		Integer courtId = detail.getCourtId();
		long s3 = System.currentTimeMillis();
		LitigationCaseCategory caseCategory = caseCategoryDao.selectByPrimaryKey(caseCategoryId);
		logger.info(String.format("获取caseCategory:%s", (System.currentTimeMillis()-s3)));
		
		long s4 = System.currentTimeMillis();
		LitigationCourt court = courtDao.selectByPrimaryKey(courtId);
		logger.info(String.format("获取court:%s", (System.currentTimeMillis()-s4)));
		Integer sourceUrlId = crawledData.getSourceUrlId();
		
		long s5 = System.currentTimeMillis();
		LitigationSourceUrl sourceUrl = sourceUrlDao.selectByPrimaryKey(sourceUrlId);
		logger.info(String.format("获取sourceUrl:%s", (System.currentTimeMillis()-s5)));

		li.setDetail(detail);
		li.setParty(parties);
		li.setCrawledData(crawledData);
		li.setCaseCategory(caseCategory);
		li.setCourt(court);
		li.setSourceUrl(sourceUrl);
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		String msg = gson.toJson(li);
		// 发送给监测消息队列
		rabbit.sendMsg(ConstantConfig.Queue.LITIGATION_MONITOR_QUEUE, msg);
		logger.info("给监测模块传输数据：id:" + detail.getId());
		return msg;
	}
}
