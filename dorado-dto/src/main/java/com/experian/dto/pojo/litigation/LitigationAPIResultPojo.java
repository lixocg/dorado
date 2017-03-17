package com.experian.dto.pojo.litigation;

public class LitigationAPIResultPojo {
	private String status;
	private String error;
	/**
	 * 
	 * 失败
	 * @param status
	 * @param error
	 */
	public LitigationAPIResultPojo(String status, String error) {
		super();
		this.status = status;
		this.error = error;
	}
	/**
	 * 成功
	 * @param status
	 */
	public LitigationAPIResultPojo(String status) {
		super();
		this.status = status;
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
	
	
}
