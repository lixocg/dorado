package com.experian.comp.elasticsearch.modle;

public class Query {
	private MultiMatch multi_match;
	
	private Bool bool;
	
	

	public Bool getBool() {
		return bool;
	}

	public void setBool(Bool bool) {
		this.bool = bool;
	}

	public MultiMatch getMulti_match() {
		return multi_match;
	}

	public void setMulti_match(MultiMatch multi_match) {
		this.multi_match = multi_match;
	}
	
	
}
