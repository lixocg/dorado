package com.experian.comp.utility;

import java.util.List;

import com.experian.comp.elasticsearch.PooledESClient;
import com.experian.comp.elasticsearch.param.ESRequest;
import com.experian.comp.elasticsearch.param.ESResponse;
import com.experian.comp.elasticsearch.param.request.Document;
import com.experian.comp.elasticsearch.param.request.SearchParam;
import com.experian.core.utils.SpringContextUtil;

/**
 * elasticsearch操作工具
 * @author lixiongcheng
 *
 */
public class ESClientUtil {
	private ESClientUtil() {
	}

	public static volatile PooledESClient esClient;

	public static PooledESClient getClient() {
		if (esClient == null) {
			synchronized (ESClientUtil.class) {
				if (esClient == null) {
					esClient = SpringContextUtil.getBean(PooledESClient.class);
				}
			}
		}
		return esClient;
	}

	public static ESResponse<Void> addDoc(ESRequest<Document> esRequest) {
		return getClient().addDoc(esRequest);
	}

	public static ESResponse<Void> addBulkDoc(ESRequest<List<Document>> esRequest) {
		return getClient().addBulkDoc(esRequest);
	}

	public static <T> ESResponse<T> search(ESRequest<SearchParam> esRequest,Class<?> clazz) {
		return getClient().search(esRequest,clazz);
	}

	
}
