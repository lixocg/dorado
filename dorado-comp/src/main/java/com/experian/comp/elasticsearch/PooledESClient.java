package com.experian.comp.elasticsearch;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.log4j.Logger;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.experian.comp.elasticsearch.config.ESConfig.ConfigInfo;
import com.experian.comp.elasticsearch.config.ESConfig.PoolConfig;
import com.experian.comp.elasticsearch.modle.MultiMatch;
import com.experian.comp.elasticsearch.modle.Query;
import com.experian.comp.elasticsearch.param.ESRequest;
import com.experian.comp.elasticsearch.param.ESResponse;
import com.experian.comp.elasticsearch.param.request.Document;
import com.experian.comp.elasticsearch.param.request.SearchParam;
import com.experian.comp.elasticsearch.param.request.SearchRequest;
import com.experian.comp.elasticsearch.param.response.Hit;
import com.experian.comp.elasticsearch.param.response.SearchResponse;
import com.experian.comp.utility.GsonUtil;
import com.experian.core.pojo.R;
import com.google.common.collect.Lists;
import com.google.gson.Gson;


/**
 * 有连接池的EsRestClient
 * 
 * @author lixiongcheng
 *
 */
@Component
public class PooledESClient implements ApplicationListener<ContextRefreshedEvent>, Ordered {
	private static final Logger logger = Logger.getLogger(PooledESClient.class);
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

	}

	public void initPooled(PoolConfig poolConfig, ConfigInfo configInfo) {
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

	/**
	 * 新增单条记录
	 * 
	 * @param esRequest
	 * @return
	 */
	public ESResponse<Void> addDoc(ESRequest<Document> esRequest) {
		RestClient restClient = null;
		ESResponse<Void> esResponse = null;
		if (pool != null) {
			try {
				restClient = pool.borrowObject();
				Gson gson = new Gson();
				HttpEntity entity = new NStringEntity(gson.toJson(esRequest.getContent()),
						ContentType.APPLICATION_JSON);
				String endpoint = "/" + esRequest.getIndex() + "/" + esRequest.getType() + "/"
						+ esRequest.getContent().getId();
				Response response = null;
				try {
					response = restClient.performRequest("PUT", endpoint, new HashMap<String, String>(), entity);
				} catch (IOException e) {
					logger.error("", e);
				}

				if (response != null) {
					String content = IOUtils.toString(response.getEntity().getContent());
					esResponse = new ESResponse<Void>(response.getStatusLine().getStatusCode() + "", content);
				} else {
					esResponse = new ESResponse<Void>(R.FAILED, "请求失败");
				}

			} catch (Exception e) {
				logger.error("", e);
			} finally {
				try {
					if (restClient != null) {
						pool.returnObject(restClient);
					}
				} catch (Exception e) {
					logger.error("", e);
				}
			}
		}
		return esResponse;
	}

	/**
	 * 批量增加记录
	 * 
	 * @param esRequest
	 * @return
	 */
	public ESResponse<Void> addBulkDoc(ESRequest<List<Document>> esRequest) {
		RestClient restClient = null;
		ESResponse<Void> esResponse = null;
		if (pool != null) {
			try {
				restClient = pool.borrowObject();

				List<Document> docs = esRequest.getContent();
				if (CollectionUtils.isEmpty(docs)) {
					return new ESResponse<Void>(R.FAILED, "无文档");
				}
				// { "index" : { "_index" : "test", "_type" : "type1", "_id" :
				// "1" } }
				// { "field1" : "value1" }
				StringBuilder sb = new StringBuilder();
				for (Document doc : docs) {
					sb.append("{ \"index\" : { \"_index\" : \"" + esRequest.getIndex() + "\", \"_type\" : \""
							+ esRequest.getType() + "\", \"_id\" : \"" + doc.getId() + "\" } }\n");
					sb.append(GsonUtil.toJson(doc.getContent()));
					sb.append("\n");
				}

				String endpoint = "/" + esRequest.getIndex() + "/" + esRequest.getType() + "/_bulk";
				Response response = null;
				try {
					HttpEntity entity = new NStringEntity(sb.toString(), ContentType.APPLICATION_JSON);
					logger.info("请求内容：" + sb.toString());
					response = restClient.performRequest("PUT", endpoint, new HashMap<String, String>(), entity);
				} catch (IOException e) {
					logger.error("", e);
				}

				if (response != null) {
					String content = IOUtils.toString(response.getEntity().getContent());
					esResponse = new ESResponse<Void>(response.getStatusLine().getStatusCode() + "", content);
				} else {
					esResponse = new ESResponse<Void>(R.FAILED, "请求失败");
				}

			} catch (Exception e) {
				logger.error("", e);
			} finally {
				try {
					if (restClient != null) {
						pool.returnObject(restClient);
					}
				} catch (Exception e) {
					logger.error("", e);
				}
			}
		}
		return esResponse;
	}

	public <T> ESResponse<T> search(ESRequest<SearchParam> esRequest,Class<?> clazz) {
		RestClient restClient = null;
		ESResponse<T> esResponse = null;
		if (pool != null) {
			try {
				restClient = pool.borrowObject();

				String endpoint = "/" + esRequest.getIndex() + "/" + esRequest.getType() + "/_search";
				SearchParam searchParam = esRequest.getContent();
				SearchRequest searchRequest = new SearchRequest();
				if (searchParam.getPage() > 0 && searchParam.getSize() > 0) {
					searchRequest.setFrom(searchParam.getPage() * searchParam.getSize());
					searchRequest.setSize(searchParam.getSize());
				}

				Query query = new Query();
				// 设置多值域关键字查询
				setMultiQuery(query, searchParam);

				searchRequest.setQuery(query);

				Response response = null;
				try {
					String requestBody = GsonUtil.toJson(searchRequest);
					System.out.println("searchRequest:" + requestBody);
					HttpEntity entity = new NStringEntity(requestBody, ContentType.APPLICATION_JSON);
					response = restClient.performRequest("GET", endpoint, new HashMap<String, String>(), entity);
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (response != null) {
					String content = IOUtils.toString(response.getEntity().getContent());
					System.out.println(content);
					SearchResponse<T> searchResponse = GsonUtil.fromJson(content, SearchResponse.class, clazz);
					esResponse = new ESResponse<>();
					esResponse.setCode(response.getStatusLine().getStatusCode()+"");
					if(searchResponse!=null){
						esResponse.setTotal(searchResponse.getHits().getTotal());
						esResponse.setTook(searchResponse.getTook());
						List<T> data = Lists.newArrayList();
						for(Hit<T> hit : searchResponse.getHits().getHits()){
							data.add(hit.get_source());
						}
						esResponse.setData(data);
					}else{
						esResponse.setMsg("查询异常");
					}
				} else {
					esResponse = new ESResponse<>(R.FAILED, "请求失败");
				}

			} catch (Exception e) {
				logger.error("", e);
			} finally {
				try {
					if (restClient != null) {
						pool.returnObject(restClient);
					}
				} catch (Exception e) {
					logger.error("", e);
				}
			}
		}
		return esResponse;
	}

	private void setMultiQuery(Query query, SearchParam searchParam) {
		if (StringUtils.isNotBlank(searchParam.getQuery())) {
			MultiMatch multi_match = new MultiMatch();
			multi_match.setFields(searchParam.getFileds());
			multi_match.setQuery(searchParam.getQuery());
			query.setMulti_match(multi_match);
		}
	}
	

}
