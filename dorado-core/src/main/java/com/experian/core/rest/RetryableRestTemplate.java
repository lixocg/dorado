package com.experian.core.rest;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriTemplateHandler;
import org.springframework.web.util.UriTemplateHandler;

import com.experian.core.enums.Srv;
import com.experian.core.exception.ServiceException;
import com.experian.core.pojo.ServiceInfo;
import com.experian.core.services.DiscoveryService;

/**
 * 封装RestTemplate.集群在某些机器宕机或者超时的情况下轮询重试
 */
@Component
public class RetryableRestTemplate {

	private Logger logger = LoggerFactory.getLogger(RetryableRestTemplate.class);

	private UriTemplateHandler uriTemplateHandler = new DefaultUriTemplateHandler();

	private RestTemplate restTemplate;

	@Autowired
	private RestTemplateFactory restTemplateFactory;

	@Autowired
	private DiscoveryService discovery;

	@PostConstruct
	private void postConstruct() {
		restTemplate = restTemplateFactory.getObject();
	}

	public <T> T get(Srv srv, String path, Class<T> responseType, Object... urlVariables)
			throws RestClientException {
		return execute(HttpMethod.GET, srv, path, null, responseType, urlVariables);
	}

	public <T> ResponseEntity<T> get(Srv srv, String path, ParameterizedTypeReference<T> reference,
			Object... uriVariables) throws RestClientException {
		return exchangeGet(srv, path, reference, uriVariables);
	}

	public <T> T post(Srv srv, String path, Object request, Class<T> responseType, Object... uriVariables)
			throws RestClientException {
		return execute(HttpMethod.POST, srv, path, request, responseType, uriVariables);
	}
	
	public void put(Srv srv, String path, Object request, Object... urlVariables)
			throws RestClientException {
		execute(HttpMethod.PUT, srv, path, request, null, urlVariables);
	}

	public void delete(Srv srv, String path, Object... urlVariables) throws RestClientException {
		execute(HttpMethod.DELETE, srv, path, null, null, urlVariables);
	}

	private <T> T execute(HttpMethod method, Srv srv, String path, Object request, Class<T> responseType,
			Object... uriVariables) {
		if (path.startsWith("/")) {
			path = path.substring(1, path.length());
		}

		String uri = uriTemplateHandler.expand(path, uriVariables).getPath();

		List<ServiceInfo> services = discovery.getServiceInstances(srv.getSrvName());

		for (ServiceInfo serviceInfo : services) {
			try {

				T result = doExecute(method, serviceInfo, path, request, responseType, uriVariables);
				return result;
			} catch (Throwable t) {
				logger.error("Http request failed, uri: {}, method: {}", uri, method, t);
			}
		}

		// all server down
		ServiceException e = new ServiceException(String.format(" servers: %s may down", services));
		throw e;
	}

	private <T> ResponseEntity<T> exchangeGet(Srv srv, String path, ParameterizedTypeReference<T> reference,
			Object... uriVariables) {
		if (path.startsWith("/")) {
			path = path.substring(1, path.length());
		}
		String uri = uriTemplateHandler.expand(path, uriVariables).getPath();
		List<ServiceInfo> services = discovery.getServiceInstances(srv.getSrvName());
		for (ServiceInfo serviceInfo : services) {
			try {
				ResponseEntity<T> result = restTemplate.exchange(parseHost(serviceInfo) + path, HttpMethod.GET, null,
						reference, uriVariables);
				return result;
			} catch (Throwable t) {
				logger.error("Http request failed, uri: {}, method: {}", uri, HttpMethod.GET, t);
			}
		}
		// all server down
		ServiceException e = new ServiceException(String.format(" servers: %s may down", services));
		throw e;
	}

	private <T> T doExecute(HttpMethod method, ServiceInfo service, String path, Object request, Class<T> responseType,
			Object... uriVariables) {
		T result = null;
		switch (method) {
		case GET:
			result = restTemplate.getForObject(parseHost(service) + path, responseType, uriVariables);
			break;
		case POST:
			result = restTemplate.postForEntity(parseHost(service) + path, request, responseType, uriVariables)
					.getBody();
			break;
		case PUT:
			restTemplate.put(parseHost(service) + path, request, uriVariables);
			break;
		case DELETE:
			restTemplate.delete(parseHost(service) + path, uriVariables);
			break;
		default:
			throw new UnsupportedOperationException(String.format("unsupported http method(method=%s)", method));
		}
		return result;
	}

	private String parseHost(ServiceInfo serviceAddress) {
		return serviceAddress.getHomePageUrl();
	}

}
