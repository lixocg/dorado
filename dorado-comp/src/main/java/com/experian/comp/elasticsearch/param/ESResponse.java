package com.experian.comp.elasticsearch.param;

import java.util.List;

import com.experian.core.pojo.R;

public class ESResponse<T> {
	private String code;

	private String msg;

	private List<T> data;

	private int total;

	private long took;

	public ESResponse() {
		this.code = R.SUCCESS;
	}

	public ESResponse(String code) {
		this.code = code;
	}

	public ESResponse(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public long getTook() {
		return took;
	}

	public void setTook(long took) {
		this.took = took;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

}
