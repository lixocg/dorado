package com.experian.comp.elasticsearch.param.response;

public class Hit<T> {
	private String _index;
	private String _type;
	private T _source;

	public String get_index() {
		return _index;
	}

	public void set_index(String _index) {
		this._index = _index;
	}

	public String get_type() {
		return _type;
	}

	public void set_type(String _type) {
		this._type = _type;
	}

	public T get_source() {
		return _source;
	}

	public void set_source(T _source) {
		this._source = _source;
	}

}
