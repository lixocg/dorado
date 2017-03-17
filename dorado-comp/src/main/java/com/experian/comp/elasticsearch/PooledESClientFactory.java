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

import com.experian.comp.elasticsearch.config.ESRestClientConfig;

@Component
public class PooledESClientFactory extends BasePooledObjectFactory<RestClient> {
	private ESRestClientConfig esRestClientConfig;

	public PooledESClientFactory(ESRestClientConfig esRestClientConfig) {
		this.esRestClientConfig = esRestClientConfig;
	}

	@Override
	public RestClient create() throws Exception {
		return createRestCient(esRestClientConfig);
	}

	@Override
	public PooledObject<RestClient> wrap(RestClient obj) {
		return new DefaultPooledObject<RestClient>(obj);
	}

	private RestClient createRestCient(ESRestClientConfig esRestClientConfig) {
		RestClientBuilder builder = RestClient.builder(
				esRestClientConfig.getHttpHosts().toArray(new HttpHost[esRestClientConfig.getHttpHosts().size()]));
		if (esRestClientConfig.isUserKey()) {
			final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(
					esRestClientConfig.getUserName(), esRestClientConfig.getPassword()));
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
