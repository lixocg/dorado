package com.experian.dto.pojo.litigation;

import java.util.List;

import com.experian.core.pojo.BaseAPIPojo;
import com.experian.dto.entity.litigation.LitigationSourceUrl;

public class SourceUrlAPIPojo extends BaseAPIPojo {
	private List<LitigationSourceUrl> data;

	public List<LitigationSourceUrl> getData() {
		return data;
	}

	public void setData(List<LitigationSourceUrl> data) {
		this.data = data;
	}
	
}
