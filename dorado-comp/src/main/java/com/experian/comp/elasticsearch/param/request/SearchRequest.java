package com.experian.comp.elasticsearch.param.request;

import java.util.HashMap;
import java.util.Map;

import com.experian.comp.elasticsearch.modle.Query;

/**
 * 最终请求elasticsearch查询参数
 * @author lixiongcheng
 *
 */
public class SearchRequest {
	private Query query;

	private Integer from = 0;

	private Integer size = 10;

	// 聚合（分组查询用）
	private Map<String, Map<String, Map<String, Object>>> aggs = new HashMap<String, Map<String, Map<String, Object>>>();

	public Map<String, Map<String, Map<String, Object>>> getAggs() {
		return aggs;
	}

	public void setAggs(Map<String, Map<String, Map<String, Object>>> aggs) {
		this.aggs = aggs;
	}

	public Integer getFrom() {
		return from;
	}

	public void setFrom(Integer from) {
		this.from = from;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}

}
