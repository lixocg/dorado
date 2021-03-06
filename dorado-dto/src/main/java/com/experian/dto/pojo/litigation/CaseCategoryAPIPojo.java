package com.experian.dto.pojo.litigation;

import java.util.List;

import com.experian.core.pojo.BaseAPIPojo;
import com.experian.dto.entity.litigation.LitigationCaseCategory;

public class CaseCategoryAPIPojo extends BaseAPIPojo {
	private List<LitigationCaseCategory> data;

	public List<LitigationCaseCategory> getData() {
		return data;
	}

	public void setData(List<LitigationCaseCategory> data) {
		this.data = data;
	}	
	
}
