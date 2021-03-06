package com.experian.daas.litigation.service;

import java.io.Serializable;

public interface LitigationErrorService {
	@SuppressWarnings("serial")
	public static class ErrorStatistic implements Serializable {
		private int errorDetailNum;
		private int errorCrawledDataNum;
		private int errorPartyNum;

		public int getErrorDetailNum() {
			return errorDetailNum;
		}

		public void setErrorDetailNum(int errorDetailNum) {
			this.errorDetailNum = errorDetailNum;
		}

		public int getErrorCrawledDataNum() {
			return errorCrawledDataNum;
		}

		public void setErrorCrawledDataNum(int errorCrawledDataNum) {
			this.errorCrawledDataNum = errorCrawledDataNum;
		}

		public int getErrorPartyNum() {
			return errorPartyNum;
		}

		public void setErrorPartyNum(int errorPartyNum) {
			this.errorPartyNum = errorPartyNum;
		}

	}

	/**
	 * 获取诉讼异常统计信息
	 * @param errorType
	 * @return
	 */
	ErrorStatistic getErrorStatistic(int errorType);
}
