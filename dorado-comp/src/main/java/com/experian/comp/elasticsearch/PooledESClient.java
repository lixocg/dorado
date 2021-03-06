package com.experian.comp.elasticsearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.log4j.Logger;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.experian.comp.elasticsearch.enums.RequestMethod;
import com.experian.comp.elasticsearch.mapping.MappingHolder;
import com.experian.comp.elasticsearch.modle.Bool;
import com.experian.comp.elasticsearch.modle.Condition;
import com.experian.comp.elasticsearch.modle.Filter;
import com.experian.comp.elasticsearch.modle.MultiMatch;
import com.experian.comp.elasticsearch.modle.Must;
import com.experian.comp.elasticsearch.modle.Nested;
import com.experian.comp.elasticsearch.modle.NestedQuery;
import com.experian.comp.elasticsearch.modle.Query;
import com.experian.comp.elasticsearch.modle.Should;
import com.experian.comp.elasticsearch.param.ESRequest;
import com.experian.comp.elasticsearch.param.ESResponse;
import com.experian.comp.elasticsearch.param.request.AggsParam;
import com.experian.comp.elasticsearch.param.request.BoolParam;
import com.experian.comp.elasticsearch.param.request.Document;
import com.experian.comp.elasticsearch.param.request.SearchParam;
import com.experian.comp.elasticsearch.param.request.SearchRequest;
import com.experian.comp.elasticsearch.param.response.Hit;
import com.experian.comp.elasticsearch.param.response.SearchResponse;
import com.experian.core.pojo.R;
import com.experian.core.utils.GsonUtil;
import com.google.common.collect.Lists;

/**
 * 有连接池的EsRestClient
 * 
 * @author lixiongcheng
 *
 */
@Component
public class PooledESClient extends AbstractPooledESClient {
	private static final Logger logger = Logger.getLogger(PooledESClient.class);

	/**
	 * 索引是否存在
	 * 
	 * @param index
	 *            索引名
	 * @return
	 */
	public boolean ifIndexExist(String index) {
		String endpoint = "/" + index;
		Response response = super.performRequest(RequestMethod.GET, endpoint, index);
		if (response != null && response.getStatusLine().getStatusCode() == 200) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 删除索引
	 * 
	 * @param index
	 */
	public void removeIndex(String index) {
		String endpoint = "/" + index;
		this.performRequest(RequestMethod.DELETE, endpoint, index);
	}

	/**
	 * 创建一个mapping
	 * 
	 * @param esRequest
	 *            请求题，需要有index，type，并且和@Document注解的indexName，type相同
	 * @param cls
	 *            那个实体对象的映射成es mapping
	 * @return
	 */
	public ESResponse<Void> createMapping(ESRequest<?> esRequest, Class<?> cls) {
		RestClient restClient = null;
		ESResponse<Void> esResponse = null;
		try {
			restClient = getRestClient();
			String docJson = MappingHolder.getInstance().getMapping(cls);
			HttpEntity entity = new NStringEntity(docJson, ContentType.APPLICATION_JSON);
			System.out.println("createMapping:" + docJson);
			String endpoint = "/" + esRequest.getIndex();
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
			returnObject(restClient);
		}
		return esResponse;
	}

	/**
	 * 新增单条记录
	 * 
	 * @param esRequest
	 * @return
	 */
	public <T> ESResponse<Void> addDoc(ESRequest<Document<T>> esRequest) {
		ESResponse<Void> esResponse = null;
		String docJson = GsonUtil.toJson(esRequest.getContent().getDoc());
		System.out.println("addDoc:" + docJson);
		String endpoint = "/" + esRequest.getIndex() + "/" + esRequest.getType() + "/" + esRequest.getContent().getId();
		Response response = null;
		response = this.performRequest(RequestMethod.PUT, endpoint, docJson);

		if (response != null) {
			String content = null;
			try {
				content = IOUtils.toString(response.getEntity().getContent());
			} catch (UnsupportedOperationException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			esResponse = new ESResponse<Void>(response.getStatusLine().getStatusCode() + "", content);
		} else {
			esResponse = new ESResponse<Void>(R.FAILED, "请求失败");
		}

		return esResponse;
	}

	/**
	 * 批量增加记录
	 * 
	 * @param esRequest
	 * @return
	 */
	public <T> ESResponse<Void> addBulkDoc(ESRequest<List<Document<T>>> esRequest) {
		ESResponse<Void> esResponse = null;

		List<Document<T>> docs = esRequest.getContent();
		if (CollectionUtils.isEmpty(docs)) {
			return new ESResponse<Void>(R.FAILED, "无文档");
		}
		StringBuilder sb = new StringBuilder();
		for (Document<T> doc : docs) {
			sb.append("{ \"index\" : { \"_index\" : \"" + esRequest.getIndex() + "\", \"_type\" : \""
					+ esRequest.getType() + "\", \"_id\" : \"" + doc.getId() + "\" } }\n");
			sb.append(GsonUtil.toJson(doc.getDoc()));
			sb.append("\n");
		}

		String endpoint = "/" + esRequest.getIndex() + "/" + esRequest.getType() + "/_bulk";
		Response response = null;
		logger.info("addBulkDoc：" + sb.toString());
		response = this.performRequest(RequestMethod.PUT, endpoint, sb.toString());

		if (response != null) {
			String content = null;
			try {
				content = IOUtils.toString(response.getEntity().getContent());
			} catch (UnsupportedOperationException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			esResponse = new ESResponse<Void>(response.getStatusLine().getStatusCode() + "", content);
		} else {
			esResponse = new ESResponse<Void>(R.FAILED, "请求失败");
		}

		return esResponse;
	}

	/**
	 * 搜索
	 * 
	 * @param esRequest
	 * @param clazz
	 *            返回实体
	 * @return
	 */
	public <T> ESResponse<T> search(ESRequest<SearchParam> esRequest, Class<?> clazz) {
		ESResponse<T> esResponse = null;
		SearchParam searchParam = esRequest.getContent();
		SearchRequest searchRequest = new SearchRequest();
		if (searchParam.getPage() > 0 && searchParam.getSize() > 0) {
			searchRequest.setFrom(searchParam.getPage() * searchParam.getSize());
			searchRequest.setSize(searchParam.getSize());
		}

		Query query = new Query();
		// 设置多值域关键字查询(keywords)
		setMultiQuery(query, searchParam);

		// 设置组合查询(bool)
		setBoolQuery(query, searchParam);

		// 设置聚合
		setGroupBy(searchRequest, searchParam);

		searchRequest.setQuery(query);

		String endpoint = "/" + esRequest.getIndex() + "/" + esRequest.getType() + "/_search";
		String requestBody = GsonUtil.toJson(searchRequest);
		System.out.println("searchRequest:" + requestBody);
		Response response = super.performRequest(RequestMethod.GET, endpoint, requestBody);

		if (response != null) {
			String content = null;
			try {
				content = IOUtils.toString(response.getEntity().getContent());
			} catch (UnsupportedOperationException | IOException e) {
				e.printStackTrace();
			}
			SearchResponse<T> searchResponse = GsonUtil.fromJson(content, SearchResponse.class, clazz);
			esResponse = new ESResponse<>();
			esResponse.setCode(response.getStatusLine().getStatusCode() + "");
			if (searchResponse != null) {
				esResponse.setTotal(searchResponse.getHits().getTotal());
				esResponse.setTook(searchResponse.getTook());
				List<T> data = Lists.newArrayList();
				for (Hit<T> hit : searchResponse.getHits().getHits()) {
					data.add(hit.get_source());
				}
				esResponse.setData(data);
			} else {
				esResponse.setMsg("查询异常");
			}
		} else {
			esResponse = new ESResponse<>(R.FAILED, "请求失败");
		}
		return esResponse;
	}

	private void setGroupBy(SearchRequest searchRequest, SearchParam searchParam) {
		if (!CollectionUtils.isEmpty(searchParam.getAggs())) {
			Map<String, Map<String, Map<String, Object>>> aggs = new HashMap<>();
			searchRequest.setAggs(aggs);
			for (AggsParam agg : searchParam.getAggs()) {
				if (agg.isNested()) {
					Map<String, Map<String, Object>> value = new HashMap<>();

					Map<String, Object> pathMap = new HashMap<>();
					pathMap.put("path", agg.getNestedPath());
					value.put("nested", pathMap);

					Map<String, Object> aggNestedMap = new HashMap<>();
					Map<String, Object> aggNestedValueMap = new HashMap<>();
					Map<String, Object> aggNestedValueFieldMap = new HashMap<>();
					aggNestedValueFieldMap.put("field", agg.getNestedPath() + "." + agg.getKey());
					aggNestedValueMap.put("terms", aggNestedValueFieldMap);
					aggNestedMap.put("group_by_" + agg.getKey(), aggNestedValueMap);
					value.put("aggs", aggNestedMap);

					aggs.put(agg.getKey(), value);

				} else {

					Map<String, Map<String, Object>> value = new HashMap<>();
					Map<String, Object> termValue = new HashMap<>();
					termValue.put("field", agg.getKey());
					value.put("terms", termValue);
					aggs.put(agg.getKey(), value);
				}
			}
		}
	}

	private void setBoolQuery(Query query, SearchParam searchParam) {
		List<BoolParam> boolParams = searchParam.getBools();
		List<Filter> filters = new ArrayList<>();
		List<Must> musts = new ArrayList<>();
		List<Should> shoulds = new ArrayList<>();
		Byte minimum_should_match = null;
		if (!CollectionUtils.isEmpty(boolParams)) {
			for (BoolParam f : boolParams) {
				// 1:must,2:filter,3:should
				switch (f.getBoolType()) {
				case 1:
					Must must = new Must();
					setCondition(must, f);
					musts.add(must);
					break;

				case 2:
					Filter filter = new Filter();
					setCondition(filter, f);
					filters.add(filter);
					break;

				case 3:
					Should s = new Should();
					setCondition(s, f);
					shoulds.add(s);
					minimum_should_match = f.getMinimumShouldMatch();
					break;
				}

			}
			Bool bool = new Bool();
			if (filters.size() > 0) {
				bool.setFilter(filters);
			}
			if (shoulds.size() > 0) {
				bool.setShould(shoulds);
				bool.setMinimum_should_match(minimum_should_match);
			}
			if (musts.size() > 0) {
				bool.setMust(musts);
			}
			query.setBool(bool);
		}
	}

	private void setCondition(Condition con, BoolParam f) {
		if (f.isNested()) {
			setNestedFilter(con, f);
			return;
		} else {
			// 过滤类型，1-mathc（根据分词），2-term（完全匹配）,3-multi_match,4-range
			switch (f.getType()) {
			case 1:
				Map<String, Object> match = new HashMap<>();
				match.put(f.getKey(), f.getValue());
				con.setMatch(match);
				break;

			case 2:
				Map<String, Object> term = new HashMap<>();
				term.put(f.getKey(), f.getValue());
				con.setTerm(term);
				break;

			case 3:
				MultiMatch multi_match = new MultiMatch();
				multi_match.setFields(new String[] {});
				multi_match.setQuery(f.getValue().toString());
				con.setMulti_match(multi_match);
				break;

			case 4:
				Map<String, Object> range = new HashMap<>();
				Map<String, Object> rangMap = new HashMap<>();
				range.put(f.getKey(), rangMap);
				con.setRange(range);
				// 1:大于等于-小于等于，2：大于等于-小于,3：大于-小于等于,4:大于-小于
				switch (f.getRangeType()) {
				case 1:
					rangMap.put("gte", f.getMinValue());
					rangMap.put("lte", f.getMaxValue());
					break;

				case 2:
					rangMap.put("gt", f.getMinValue());
					rangMap.put("lte", f.getMaxValue());
					break;

				case 3:
					rangMap.put("gt", f.getMinValue());
					rangMap.put("lte", f.getMaxValue());
					break;

				case 4:
					rangMap.put("gt", f.getMinValue());
					rangMap.put("lt", f.getMaxValue());
					break;
				}
				if (!StringUtils.isEmpty(f.getFormat())) {
					rangMap.put("format", f.getFormat());
				}
				break;

			default:
				break;
			}
		}
	}

	private void setNestedFilter(Condition con, BoolParam f) {
		Filter filter = new Filter();
		if (f.getType() == 1) {// match
			Map<String, Object> match = new HashMap<>();
			match.put(f.getNestedPath() + "." + f.getKey(), f.getValue());
			filter.setMatch(match);
		}
		if (f.getType() == 2) {// term
			Map<String, Object> term = new HashMap<>();
			term.put(f.getNestedPath() + "." + f.getKey(), f.getValue());
			filter.setTerm(term);
		}

		Nested nested = new Nested();
		nested.setPath(f.getNestedPath());
		NestedQuery nestedQuery = new NestedQuery();
		Bool nestedBool = new Bool();
		List<Filter> nestedFilters = new ArrayList<>();
		nestedFilters.add(filter);
		nestedBool.setFilter(nestedFilters);
		nestedQuery.setBool(nestedBool);
		nested.setQuery(nestedQuery);

		con.setNested(nested);
	}

	/**
	 * 设置多关键字匹配查询
	 * 
	 * @param query
	 *            查询关键字
	 * @param searchParam
	 *            查询参数
	 */
	private void setMultiQuery(Query query, SearchParam searchParam) {
		if (StringUtils.isNotBlank(searchParam.getKeyword())) {
			MultiMatch multi_match = new MultiMatch();
			multi_match.setFields(searchParam.getFileds());
			multi_match.setQuery(searchParam.getKeyword());
			query.setMulti_match(multi_match);
		}
	}

}
