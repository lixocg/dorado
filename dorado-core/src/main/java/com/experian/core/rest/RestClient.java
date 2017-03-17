package com.experian.core.rest;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import com.experian.core.enums.Srv;
import com.experian.core.utils.SpringContextUtil;

public class RestClient {
	/**
	 * 获得一个RetryableRestTemplate客户端
	 *
	 * @return
	 */
	public static RetryableRestTemplate getClient() {
		RetryableRestTemplate restTemplate = (RetryableRestTemplate) SpringContextUtil.getBean("retryableRestTemplate");
		return restTemplate;
	}

	public static <T> T get(Srv srv, String path, Class<T> responseType, Object... urlVariables)
			throws RestClientException {
		return getClient().get(srv, path, responseType, urlVariables);
	}

	public static <T> ResponseEntity<T> get(Srv srv, String path, ParameterizedTypeReference<T> reference,
			Object... uriVariables) throws RestClientException {
		return getClient().get(srv, path, reference, uriVariables);
	}

	public static <T> T post(Srv srv, String path, Object request, Class<T> responseType, Object... uriVariables)
			throws RestClientException {
		return getClient().post(srv, path, request, responseType, uriVariables);
	}

	public static void put(Srv srv, String path, Object request, Object... urlVariables) throws RestClientException {
		getClient().put(srv, path, request, urlVariables);
	}

	public static void delete(Srv srv, String path, Object... urlVariables) throws RestClientException {
		getClient().delete(srv, path, urlVariables);
	}

}