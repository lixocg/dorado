package com.experian.comp.test;


import com.experian.comp.elasticsearch.annotation.Field;
import com.experian.comp.elasticsearch.annotation.FieldIndex;
import com.experian.comp.elasticsearch.annotation.Nested;

@Nested(name="parties")
public class Party {
	@Field(index = FieldIndex.not_analyzed, store = true)
	private String id;

	@Field(index = FieldIndex.not_analyzed, store = true)
	private String name;

	@Field(index = FieldIndex.not_analyzed, store = true)
	private String sbd;

	@Field(index = FieldIndex.not_analyzed, store = true)
	private int type;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSbd() {
		return sbd;
	}

	public void setSbd(String sbd) {
		this.sbd = sbd;
	}

}
