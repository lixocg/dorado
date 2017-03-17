package com.experian.daas.litigation.handler;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.experian.comp.rabbitmq.ConsumerHandler;
import com.experian.comp.utility.RedisUtil;
import com.experian.core.database.sharding.sqlsessiontemplate.CustomerContextHolder;
import com.experian.core.enums.Srv;
import com.experian.core.rest.RestClient;
import com.experian.daas.litigation.dao.LitigationPartyDao;
import com.experian.dto.entity.litigation.LitigationParty;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

public class MatchSbdnumHandler implements ConsumerHandler {
	private static final Logger logger = Logger.getLogger(MatchSbdnumHandler.class);
	
	private LitigationPartyDao partyDao;

	public MatchSbdnumHandler(LitigationPartyDao partyDao) {
		this.partyDao = partyDao;
	}

	@Override
	public void handle(String msg) {
		List<LitigationParty> ps = new Gson().fromJson(msg, new TypeToken<List<LitigationParty>>() {
			private static final long serialVersionUID = 1288919860517120589L;
		}.getType());
		if (!CollectionUtils.isEmpty(ps)) {
			for (LitigationParty p : ps) {
				long s = System.currentTimeMillis();
				String name = p.getName();
				String sbdnum = null;
				if (StringUtils.isNotBlank(name)) {
					// 先从redis中获取
					sbdnum = RedisUtil.get(name);
					// redis 中没有，在请求服务获取
					if (StringUtils.isNotBlank(sbdnum)) {
						sbdnum = RestClient.get(Srv.baseinfo, "/baseinfo/match/{name}", String.class, name);
					}
				}
				if (StringUtils.isEmpty(sbdnum)) {
					continue;
				}
				//得到sbdnum，放入rediszhong
				RedisUtil.set(name, sbdnum);
				p.setSbdnum(sbdnum);
				CustomerContextHolder.setContextType(CustomerContextHolder.MYSQL);
				partyDao.updateSbdnumByPrimaryKey(p.getSbdnum(), p.getId());
				logger.info(String.format("name:%s ---匹配sbdnum：%s,耗时:", name,sbdnum,(System.currentTimeMillis()-s)));
			}
		}
	}

}
