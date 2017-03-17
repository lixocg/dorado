package com.experian.dto.pojo.litigation;

import java.io.Serializable;
import java.util.List;

import com.experian.core.pojo.BaseAPIPojo;

/**
 * optix 传来诉讼数据包
 * 
 * @author e00769a
 *
 */
@SuppressWarnings("serial")
public class LitigationAPIPojos extends BaseAPIPojo implements Serializable {
	private List<LitigationAPIPojo> data;

	public List<LitigationAPIPojo> getData() {
		return data;
	}

	public void setData(List<LitigationAPIPojo> data) {
		this.data = data;
	}

}
