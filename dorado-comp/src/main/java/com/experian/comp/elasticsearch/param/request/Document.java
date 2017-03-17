package com.experian.comp.elasticsearch.param.request;

/**
 * 构建文档
 * 
 * @author lixiongcheng
 *
 */
public class Document<T> {
	private String id;
	private T content;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public T getContent() {
		return content;
	}

	public void setContent(T content) {
		this.content = content;
	}

}
