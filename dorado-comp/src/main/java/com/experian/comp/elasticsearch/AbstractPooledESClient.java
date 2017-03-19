package com.experian.comp.elasticsearch;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.log4j.Logger;
import org.elasticsearch.client.RestClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;

import com.experian.comp.elasticsearch.config.ESConfig.ConfigInfo;
import com.experian.comp.elasticsearch.config.ESConfig.PoolConfig;
import com.experian.comp.elasticsearch.mapping.MappingScan;

public abstract class AbstractPooledESClient implements ApplicationEventPublisherAware, ApplicationListener<ContextRefreshedEvent>,Ordered {
	private static final Logger logger = Logger.getLogger(AbstractPooledESClient.class);
	
	
	private GenericObjectPool<RestClient> pool;



	@Override
	public int getOrder() {
		return 0;
	}




	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ApplicationContext applicationContext = event.getApplicationContext();
		PoolConfig poolConfig = applicationContext.getBean(PoolConfig.class);
		ConfigInfo configInfo = applicationContext.getBean(ConfigInfo.class);
		initPooled(poolConfig, configInfo);
		//初始化需要创建的文档
		new MappingScan(new String[]{"com.experian.comp.test"});
	}




	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		
	}

	private void initPooled(PoolConfig poolConfig, ConfigInfo configInfo) {
		synchronized (this) {
			if (this.pool == null) {
				pool = new GenericObjectPool<RestClient>(new PooledESClientFactory(configInfo));
				pool.setMaxIdle(poolConfig.getMaxIdle());
				pool.setMinIdle(poolConfig.getMinIdle());
				pool.setMaxTotal(poolConfig.getMaxTotal());
				pool.setMaxWaitMillis(poolConfig.getMaxWaitMillis());
			}
		}
	}
	
	public RestClient getRestClient(){
		try {
			return pool.borrowObject();
		} catch (Exception e) {
			throw new RuntimeException("",e);
		}
	}
	
	public void returnObject(RestClient restClient){
		try {
			if (restClient != null) {
				pool.returnObject(restClient);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
}
