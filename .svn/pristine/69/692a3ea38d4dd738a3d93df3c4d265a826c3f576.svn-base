package com.experian.core.pojo;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class ApiResult<T> implements Serializable {
	private String status;
	private List<Errors> error;
	private T data;// 返回数据，没有返回数据，用Void类型

	/**
	 * 成功，无数据
	 */
	public ApiResult() {
		this.status = R.SUCCESS;
	}

	/**
	 * 成功，有数据
	 * 
	 * @param data
	 */
	public ApiResult(T data) {
		this.status = R.SUCCESS;
		this.data = data;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Errors> getError() {
		return error;
	}

	public void setError(List<Errors> error) {
		this.error = error;
	}

	public static class Errors {
		public static class Type {
			public static final String TOKEN_UNPASSED = "102";
			public static final String DUPLICATE_KEY = "101";
			public static final String OTHER = "100";
		}

		private String type;
		private String id;
		private String message;

		public Errors() {
		}

		public Errors(String type, String id, String message) {
			this.type = type;
			this.id = id;
			this.message = message;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

	}
}
