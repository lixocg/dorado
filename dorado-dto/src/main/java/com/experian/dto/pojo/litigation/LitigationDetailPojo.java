package com.experian.dto.pojo.litigation;

import com.experian.core.enums.CategorySort;
import com.experian.dto.entity.litigation.LitigationParsedDataDetail;

public class LitigationDetailPojo implements Comparable<LitigationDetailPojo> {
	public static final String FROM = "1";
	public static final String HISTORY = "2";

	public LitigationDetailPojo(LitigationParsedDataDetail detail, String type) {
		this.detail = detail;
		this.type = type;
	}
	

	/**
	 * 诉讼对象
	 */
	private LitigationParsedDataDetail detail;
	/**
	 * 数据类型 1：来源数据 2：先有旧数据
	 */
	private String type;

	public LitigationParsedDataDetail getDetail() {
		return detail;
	}

	public String getType() {
		return type;
	}

	//案件类型排序重新实现
	public int compareTo(LitigationDetailPojo pojo) {
		return CategorySort.indexof(this.detail.getCaseCategoryId()) - CategorySort.indexof(pojo.getDetail().getCaseCategoryId());
	}

	//用于案件类型判断是否包含
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final LitigationDetailPojo other = (LitigationDetailPojo) obj;
		if (detail == null) {
			return other.detail == null;
		} else if (detail.getCaseCategoryId() == null) {
			return other.detail.getCaseCategoryId() == null;
		}
		return CategorySort.indexof(detail.getCaseCategoryId()) == CategorySort.indexof(other.detail.getCaseCategoryId());
	}

}