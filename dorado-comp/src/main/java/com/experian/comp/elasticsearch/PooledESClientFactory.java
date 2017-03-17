package com.experian.comp.elasticsearch;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestClientBuilder.HttpClientConfigCallback;
import org.springframework.stereotype.Component;

import com.experian.comp.elasticsearch.config.ESConfig.ConfigInfo;

@Component
public class PooledESClientFactory extends BasePooledObjectFactory<RestClient> {
	private ConfigInfo configInfo;

	public PooledESClientFactory(ConfigInfo configInfo) {
		this.configInfo = configInfo;
	}

	@Override
	public RestClient create() throws Exception {
		return createRestCient(configInfo);
	}

	@Override
	public PooledObject<RestClient> wrap(RestClient obj) {
		return new DefaultPooledObject<RestClient>(obj);
	}

	private RestClient createRestCient(ConfigInfo configInfo) {
		RestClientBuilder builder = RestClient.builder(
				configInfo.getHttpHosts().toArray(new HttpHost[configInfo.getHttpHosts().size()]));
		if (configInfo.isUserKey()) {
			final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(
					configInfo.getUserName(), configInfo.getPassword()));
			builder.setHttpClientConfigCallback(new HttpClientConfigCallback() {
				@Override
				public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
					return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
				}
			});
		}
		return builder.build();
	}

}
