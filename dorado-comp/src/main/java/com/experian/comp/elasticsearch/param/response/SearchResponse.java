package com.experian.comp.elasticsearch.param.response;

public class SearchResponse<T> {

	private Hits<T> hits;

	private long took;// 花费时间

	public long getTook() {
		return took;
	}

	public void setTook(long took) {
		this.took = took;
	}

	public Hits<T> getHits() {
		return hits;
	}

	public void setHits(Hits<T> hits) {
		this.hits = hits;
	}
}
