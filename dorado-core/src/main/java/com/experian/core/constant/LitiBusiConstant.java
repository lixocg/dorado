package com.experian.core.constant;

public class LitiBusiConstant {

	/**
	 * 数据解析状态
	 * 
	 * @author e00769a
	 *
	 */
	public static class ParsedStatus {
		/**
		 * 解析成功
		 */
		public static final Integer SUCCESS = 0;
		/**
		 * 未解析成功，但有用
		 */
		public static final Integer FAILED_BUT_NEEDED = 1;
		/**
		 * 无用数据
		 */
		public static final Integer UN_NEEDED = 3;
	}

	/**
	 * 企业、自然人类别
	 * 
	 * @author e00769a
	 *
	 */
	public static class PartyCategory {
		/**
		 * 原告
		 */
		public static final Integer ACCUSER = 1;
		/**
		 * 被告
		 */
		public static final Integer ACCUSED = 2;
		/**
		 * 当事人
		 */
		public static final Integer PARTIES = 3;
	}

	/**
	 * 数据抓取状态
	 * 
	 * @author e00769a
	 *
	 */
	public static class DataFetchStatus {
		/**
		 * 还未抓取
		 */
		public static final int UN_FETCH = 0;
		/**
		 * 已被抓取
		 */
		public static final int FETCHED = 1;
		/**
		 * 需要重新抓取
		 */
		public static final int RE_FETCH = 2;
	}

	/**
	 * 异常类型，用于error表
	 * 
	 * @author e00769a
	 *
	 */
	public static enum ErrorType {
		/**
		 * 因为去重被物理删除了
		 */
		deplication(0),
		/**
		 * 因为数据不规范导致程序抛出异常导致
		 */
		dataNotCriteria(1);

		private int type;

		private ErrorType(int type) {
			this.type = type;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public static boolean contains(int type) {
			ErrorType[] values = ErrorType.values();
			for (ErrorType typ : values) {
				if (type == typ.ordinal()) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * 数据来源标示
	 * 
	 * @author e00769a
	 *
	 */
	public static class DataFrom {
		/**
		 * optix
		 */
		public static final int OPTIX = 0;
		/**
		 * db2主表
		 */
		public static final int DB2_LITIGATION = 1;
		/**
		 * db2进程表
		 */
		public static final int DB2_LITIGATION_HISTORY = 2;
		/**
		 * sqlserver诉讼平台
		 */
		public static final int SQLSERVER_LITIGATION = 3;
		/**
		 * 调研员
		 */
		public static final int MANUAL = 4;
	}

	/**
	 * 数据来源主键标示前缀
	 * 
	 * @author e00769a
	 *
	 */
	public static class Prefix {
		public static final String OPTIX = "O";
		public static final String EXPERIAL = "E";
	}

	/**
	 * 诉讼详情解析状态
	 * 
	 * @author e00769a
	 *
	 */
	public static class DataStatus {
		/**
		 * “0”表示该条数据解析成功；
		 */
		public static final int SUCCESS = 0;
		/**
		 * “1”，表示该条数据有用但未解析成功；
		 */
		public static final int AVAILABLE_FAIL = 1;
		/**
		 * “3”表示无用数据
		 */
		public static final int UNAVAILABLE = 3;
	}

}
