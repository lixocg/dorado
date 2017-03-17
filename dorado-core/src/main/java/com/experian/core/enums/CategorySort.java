package com.experian.core.enums;
/**
 * 案件类型排序枚举
 * @author e00898a
 *
 */
public enum CategorySort {
	CPWS(",1,2,3,4,5,6,", 1)// 裁判文书
	, ZZGG(",7,", 2)// 执行公告
	, SXGG("", 3)// 失信公告
	, XZGXF("", 4)// 限制高消费
	, KTGG("", 5)// 开庭公告
	, FYGG("", 6);// 法院公告
	CategorySort(String id, int index) {
		this.id = id;
		this.index = index;
	}

	private String id;
	private int index;

	public String getId() {
		return id;
	}

	public int getIndex() {
		return index;
	}

	public static int indexof(int id) {
		for (CategorySort cs : values()) {
			if (cs.getId().contains("," + id + ",")) {
				return cs.getIndex();
			}
		}
		return 999999;
	}


}