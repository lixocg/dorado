package com.experian.comp.elasticsearch.modle;

public class Nested {
	private String path;

	private NestedQuery query;

	public NestedQuery getQuery() {
		return query;
	}

	public void setQuery(NestedQuery query) {
		this.query = query;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
