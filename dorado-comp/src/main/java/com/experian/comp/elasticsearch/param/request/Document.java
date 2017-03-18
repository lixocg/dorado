package com.experian.comp.elasticsearch.param.request;

/**
 * 构建文档
 * 
 * @author lixiongcheng
 *
 */
public class Document<T> {
	private String id;
	private T doc;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public T getDoc() {
		return doc;
	}

	public void setDoc(T doc) {
		this.doc = doc;
	}

}
