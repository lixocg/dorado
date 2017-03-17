package com.experian.core.pojo;

import java.io.Serializable;

/**
 * 返回页面数据包装类
 * 
 * @author lixiongcheng
 *
 * @param <T>
 */
@SuppressWarnings("serial")
public class R<T> implements Serializable {
	private String status; // 错误代码
	private String error; // 错误描述
	private T data;//返回数据，没有返回数据，用Void类型
	
	public static final String SUCCESS = "0";
	public static final String FAILED = "1";

	/**
	 * 成功，无数据
	 */
	public R() {
		this.status = R.SUCCESS;
	}

	/**
	 * 成功，有数据
	 * 
	 * @param data
	 */
	public R(T data) {
		this.status = R.SUCCESS;
		this.data = data;
	}

	/**
	 * 失败，错误信息
	 * 
	 * @param code
	 * @param desc
	 */
	public R(String status, String error) {
		this.status = status;
		this.error = error;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
