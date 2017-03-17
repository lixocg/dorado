package com.experian.core.enums;

/**
 * ID生成器工作站枚举类
 * @author e00898a
 *
 */
public enum WorkIdEnum {
	LITIGATION("诉讼服务器1", 1L);

	WorkIdEnum(String workName, long workId) {
		this.workName = workName;
		this.workId = workId;
	}

	/**
	 * 工作站名称
	 */
	private String workName;
	/**
	 * 工作站ID
	 */
	private long workId;

	public String getWorkName() {
		return workName;
	}

	public long getWorkId() {
		return workId;
	}

	public static String nameOf(long workId) {
		for (WorkIdEnum workIdEnum : values()) {
			if (workIdEnum.getWorkId() == workId) {
				return workIdEnum.getWorkName();
			}
		}
		return null;
	}

}
