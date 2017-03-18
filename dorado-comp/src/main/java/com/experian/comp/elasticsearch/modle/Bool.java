package com.experian.comp.elasticsearch.modle;

import java.util.List;

public class Bool {
	private List<Filter> filter;

	private List<Should> should;

	private List<Must> must;

	private Byte minimum_should_match;

	public Byte getMinimum_should_match() {
		return minimum_should_match;
	}

	public void setMinimum_should_match(Byte minimum_should_match) {
		this.minimum_should_match = minimum_should_match;
	}

	public List<Should> getShould() {
		return should;
	}

	public void setShould(List<Should> should) {
		this.should = should;
	}

	public List<Must> getMust() {
		return must;
	}

	public void setMust(List<Must> must) {
		this.must = must;
	}

	public List<Filter> getFilter() {
		return filter;
	}

	public void setFilter(List<Filter> filter) {
		this.filter = filter;
	}

}
