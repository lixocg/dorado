package com.experian.daas.litigation.config;


public class ConstantConfig {
	public static class Queue {
		/**
		 * 匹配sbdnum queueName
		 */
		public static final String MATCH_SBDNUM_QUEUE = "matchSbdnumQueue";

		/**
		 * 去重队列数量
		 */
		public static final int QUEUE_SIZE = 21;

		/**
		 * 去重队列
		 */
		public static final String QUEUE_NAME_PREFIX = "litigationQueue";
		
		/**
		 * 监测队列
		 */
		public static final String LITIGATION_MONITOR_QUEUE = "monitor_litigation_queue";
	}
}
