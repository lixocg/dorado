package com.experian.comp.elasticsearch.param.request;

import java.util.List;

import com.experian.comp.elasticsearch.modle.AggsParam;
import com.experian.comp.elasticsearch.modle.FilterParam;

public class SearchParam {
	private String index;

	private String type;

	private Integer size = 0;

	private Integer page = 0;

	private String query;// 关键词

	private String[] fileds;// 多个关键词^比重...{ "name^8", "productName^8", "desc^1"
							// }

	private int flag = 1;

	private List<FilterParam> filters;

	private List<AggsParam> aggs;
	
	

	public String[] getFileds() {
		return fileds;
	}

	public void setFileds(String[] fileds) {
		this.fileds = fileds;
	}

	public List<AggsParam> getAggs() {
		return aggs;
	}

	public void setAggs(List<AggsParam> aggs) {
		this.aggs = aggs;
	}

	public List<FilterParam> getFilters() {
		return filters;
	}

	public void setFilters(List<FilterParam> filters) {
		this.filters = filters;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
