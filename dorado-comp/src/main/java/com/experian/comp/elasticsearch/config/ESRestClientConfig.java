package com.experian.comp.elasticsearch.config;

import java.util.List;

import org.apache.http.HttpHost;

/**
 * es连接配置
 * @author lixiongcheng
 *
 */
public class ESRestClientConfig {
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
