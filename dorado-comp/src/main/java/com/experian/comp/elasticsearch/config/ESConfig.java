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
	public ConfigInfo eSRestClientConfig() {
		ConfigInfo configInfo = new ConfigInfo();
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
		configInfo.setHttpHosts(httpHosts);
		configInfo.setUserKey(Boolean.parseBoolean(env.getProperty("es.usekey", "false")));
		configInfo.setUserName(env.getProperty("es.userName"));
		configInfo.setPassword(env.getProperty("es.password"));
		return configInfo;
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

	/**
	 * 配置信息
	 * 
	 * @author lixiongcheng
	 *
	 */
	public static class ConfigInfo {
		private List<HttpHost> httpHosts;

		private boolean userKey;// 是否需要用户名密码

		private String userName;

		private String password;

		public List<HttpHost> getHttpHosts() {
			return httpHosts;
		}

		public void setHttpHosts(List<HttpHost> httpHosts) {
			this.httpHosts = httpHosts;
		}

		public boolean isUserKey() {
			return userKey;
		}

		public void setUserKey(boolean userKey) {
			this.userKey = userKey;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	}

	/**
	 * 连接池配置信息
	 * 
	 * @author lixiongcheng
	 *
	 */
	public static class PoolConfig {
		private Integer maxIdle;

		private Integer minIdle;

		private Integer maxTotal;

		private Integer maxWaitMillis;

		public Integer getMaxIdle() {
			return maxIdle;
		}

		public void setMaxIdle(Integer maxIdle) {
			this.maxIdle = maxIdle;
		}

		public Integer getMinIdle() {
			return minIdle;
		}

		public void setMinIdle(Integer minIdle) {
			this.minIdle = minIdle;
		}

		public Integer getMaxTotal() {
			return maxTotal;
		}

		public void setMaxTotal(Integer maxTotal) {
			this.maxTotal = maxTotal;
		}

		public Integer getMaxWaitMillis() {
			return maxWaitMillis;
		}

		public void setMaxWaitMillis(Integer maxWaitMillis) {
			this.maxWaitMillis = maxWaitMillis;
		}
	}
}
