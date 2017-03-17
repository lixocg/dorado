package com.experian.daas.litigation.service.impl;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.experian.comp.utility.RedisUtil;
import com.experian.core.constant.LitiBusiConstant;
import com.experian.core.pojo.R;
import com.experian.daas.litigation.service.LitigationAdapterService;
import com.experian.daas.litigation.service.LitigationBatchAdapterService;
import com.experian.dto.pojo.litigation.LitigationAPIPojo;

/**
 * 去重批量 ，已废弃
 * @author e00769a
 *
 */
@Deprecated
@Service("litigationBatchAdapterService")
public class LitigationBatchAdapterServiceImpl implements LitigationBatchAdapterService {
	private static final Logger logger = Logger.getLogger(LitigationBatchAdapterServiceImpl.class);

	@Resource
	private LitigationAdapterService litigationAdapterService;
	

	public R<Void> db2Deplicate() {

		return new R<Void>();
	}

	public static final String IS_STOP = "isStop";

	// @PostConstruct
	public void createDeduplicateTask() {
		if (!RedisUtil.exists(IS_STOP)) {
			RedisUtil.set(IS_STOP, "true");
		}
		ExecutorService executorProducer = Executors.newSingleThreadExecutor();
		executorProducer.execute(new DeduplicateTask(litigationAdapterService));
		logger.info("批量去重任务启动:" + (RedisUtil.get(IS_STOP) == "false" ? "是" : "否"));
	}

	public static class DeduplicateTask implements Runnable {
		LitigationAdapterService litigationAdapterService;

		public DeduplicateTask(LitigationAdapterService litigationAdapterService) {
			this.litigationAdapterService = litigationAdapterService;
		}

		@Override
		public void run() {
			try {
				Thread.sleep(1000 * 20);
			} catch (Exception e) {

			}
			while (true) {
				if (!Boolean.parseBoolean(RedisUtil.get(IS_STOP))) {
					logger.info("<<<<<该批量去重开始>>>>>");
					long start = System.currentTimeMillis();
					List<LitigationAPIPojo> lastest = litigationAdapterService.getLastest(200,
							LitiBusiConstant.DataFetchStatus.UN_FETCH, LitiBusiConstant.ParsedStatus.SUCCESS);
					logger.info("批量数：" + (lastest != null ? lastest.size() : 0));
					if (!CollectionUtils.isEmpty(lastest)) {
						for (LitigationAPIPojo p : lastest) {
							try {
								litigationAdapterService.executeAdapt(p);
							} catch (Exception e) {
								logger.info(e);
							}
						}
					} else {
						RedisUtil.set(IS_STOP, "true");
					}
					logger.info("<<<<<该批量去重结束>>>>>耗时:" + (System.currentTimeMillis() - start));
				}
			}
		}

	}

	@Override
	public void start(long _num) {
		RedisUtil.set(IS_STOP, "false");
	}

	@Override
	public void stop() {
		RedisUtil.set(IS_STOP, "true");
	}

}
