package com.experian.comp.elasticsearch.config;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

@Configuration
@PropertySource(value = "classpath:conf/${spring.profiles.active}/es.properties")
public class ESConfig implements EnvironmentAware {
	@Bean
	public ESRestClientConfig eSRestClientConfig() {
		ESRestClientConfig eSRestClientConfig = new ESRestClientConfig();
		// elasticsearch主机
		String hostsStr = env.getProperty("es.hosts");
		if (StringUtils.isEmpty(hostsStr)) {
			throw new RuntimeException("请检查es.properties文件es.hosts配置为空，ip1:port1,ip2:port2,...格式");
		}
		List<String> hostsList = Splitter.on(",").omitEmptyStrings().splitToList(hostsStr);
		List<HttpHost> httpHosts = Lists.newArrayList();
		for (String hostipStr : hostsList) {
			List<String> hostandip = Splitter.on(":").omitEmptyStrings().splitToList(hostipStr);
			HttpHost httpHost = new HttpHost(hostandip.get(0), Integer.parseInt(hostandip.get(1)), "http");
			httpHosts.add(httpHost);
		}
		eSRestClientConfig.setHttpHosts(httpHosts);
		eSRestClientConfig.setUserKey(Boolean.parseBoolean(env.getProperty("es.usekey", "false")));
		eSRestClientConfig.setUserName(env.getProperty("es.userName"));
		eSRestClientConfig.setPassword(env.getProperty("es.password"));
		return eSRestClientConfig;
	}

	@Bean
	public PoolConfig poolConfig() {
		PoolConfig poolConfig = new PoolConfig();
		poolConfig.setMaxIdle(Integer.parseInt(env.getProperty("es.pool.maxIdle", "10")));
		poolConfig.setMinIdle(Integer.parseInt(env.getProperty("es.pool.minIdle", "3")));
		poolConfig.setMaxTotal(Integer.parseInt(env.getProperty("es.pool.maxTotal", "500")));
		poolConfig.setMaxWaitMillis(Integer.parseInt(env.getProperty("es.pool.maxWaitMillis", "100000")));
		return poolConfig;
	}

	private Environment env;

	@Override
	public void setEnvironment(Environment environment) {
		this.env = environment;
	}
}
