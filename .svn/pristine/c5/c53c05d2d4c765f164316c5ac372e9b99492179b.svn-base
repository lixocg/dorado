package com.experian.daas.litigation.service;

import java.util.List;

import com.experian.core.pojo.ApiResult;
import com.experian.dto.entity.litigation.LitigationCaseCategory;
import com.experian.dto.entity.litigation.LitigationCourt;
import com.experian.dto.entity.litigation.LitigationPartyCategory;
import com.experian.dto.entity.litigation.LitigationSourceUrl;
import com.experian.dto.pojo.litigation.LitigationAPIPojo;

/**
 * 诉讼各渠道API 
 * @author e00898a
 *
 */
public interface LitigationChannelService {

	/**
	 * 保存案件类型
	 * @param list
	 * @return
	 */
	ApiResult<Void> litigationNewCaseCategory(List<LitigationCaseCategory> list);


	/**
	 * 新增诉讼数据来源
	 * @param url
	 * @return
	 */
	ApiResult<Void> litigationNewSourceUrl(List<LitigationSourceUrl> urls);

	/**企业自然人类型新增
	 * @param partyCategoriess
	 * @return
	 */
	ApiResult<Void> litigationNewPartyCategory(List<LitigationPartyCategory> partyCategories);

	/**
	 * 诉讼主体数据
	 * @param litigations
	 * @return
	 */
	ApiResult<Void> litigationChannel(List<LitigationAPIPojo> litigations);

	/**
	 * 法院
	 * @param courts
	 * @return
	 */
	ApiResult<Void> litigationNewCourt(List<LitigationCourt> courts);

}
