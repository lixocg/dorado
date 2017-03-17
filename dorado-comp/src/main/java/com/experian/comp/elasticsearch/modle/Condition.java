package com.experian.comp.elasticsearch.modle;

import java.util.Map;

public abstract class Condition {
	private Map<String, Object> match;
	private Map<String, Object> term;
	private MultiMatch multi_match;

	private Nested nested;
	private Map<String, Object> range;

	public Map<String, Object> getRange() {
		return range;
	}

	public void setRange(Map<String, Object> range) {
		this.range = range;
	}

	public MultiMatch getMulti_match() {
		return multi_match;
	}

	public void setMulti_match(MultiMatch multi_match) {
		this.multi_match = multi_match;
	}

	public Map<String, Object> getMatch() {
		return match;
	}

	public void setMatch(Map<String, Object> match) {
		this.match = match;
	}

	public Map<String, Object> getTerm() {
		return term;
	}

	public void setTerm(Map<String, Object> term) {
		this.term = term;
	}

	public Nested getNested() {
		return nested;
	}

	public void setNested(Nested nested) {
		this.nested = nested;
	}
}
