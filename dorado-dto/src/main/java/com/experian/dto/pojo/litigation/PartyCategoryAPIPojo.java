package com.experian.dto.pojo.litigation;

import java.util.List;

import com.experian.core.pojo.BaseAPIPojo;
import com.experian.dto.entity.litigation.LitigationPartyCategory;

public class PartyCategoryAPIPojo extends BaseAPIPojo {
	private List<LitigationPartyCategory> data;

	public List<LitigationPartyCategory> getData() {
		return data;
	}

	public void setData(List<LitigationPartyCategory> data) {
		this.data = data;
	}
	
}
