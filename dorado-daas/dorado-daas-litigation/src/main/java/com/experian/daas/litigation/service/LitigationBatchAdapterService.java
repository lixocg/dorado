package com.experian.daas.litigation.service;

public interface LitigationBatchAdapterService {

	/**
	 * 开始
	 * @param _num
	 */
	void start(long _num);

	/**
	 * 立即停止
	 */
	void stop();


}
