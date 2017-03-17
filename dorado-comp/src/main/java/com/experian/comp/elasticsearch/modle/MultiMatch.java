package com.experian.comp.elasticsearch.modle;

/**
 * 多个关键字匹配查询
 * 
 * @author lixiongcheng
 *
 */
public class MultiMatch {
	/**
	 * 查询关键字
	 */
	private String query;
	/**
	 * 需要匹配的字段，可以加权重 field^score
	 */
	private String[] fields;

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String[] getFields() {
		return fields;
	}

	public void setFields(String[] fields) {
		this.fields = fields;
	}

}
