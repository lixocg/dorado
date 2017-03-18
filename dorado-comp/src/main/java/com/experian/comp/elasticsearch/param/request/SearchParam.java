package com.experian.comp.elasticsearch.param.request;

import java.util.List;

/**
 * 查询参数
 * 
 * @author lixiongcheng
 *
 */
public class SearchParam {
	/**
	 * 分页条数
	 */
	private Integer size = 0;

	/**
	 * 分页页码
	 */
	private Integer page = 0;

	/**
	 * 关键词
	 */
	private String keyword;

	/**
	 * 多个关键词^比重...{ "name^8", "productName^8", "desc^1" }
	 */
	private String[] fileds;

	/**
	 * 组合查询(bool)参数
	 */
	private List<BoolParam> bools;

	/**
	 * 聚合查询
	 */
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

	public List<BoolParam> getBools() {
		return bools;
	}

	public void setBools(List<BoolParam> bools) {
		this.bools = bools;
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

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

}
