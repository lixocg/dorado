package com.experian.dto.pojo.litigation;

import java.util.ArrayList;
import java.util.List;

import com.experian.dto.entity.litigation.LitigationCrawledData;
import com.experian.dto.entity.litigation.LitigationParsedDataDetail;
import com.experian.dto.entity.litigation.LitigationParty;
import com.experian.dto.entity.litigation.LitigationPartyCategory;
import com.google.gson.Gson;

/**
 * 诉讼API接收数据POJO
 * 
 * @author e00898a
 *
 */
public class LitigationAPIPojo {
	// 诉讼案件详情
	private LitigationParsedDataDetail litigationParsedDataDetail;
	// 诉讼抓取数据
	private LitigationCrawledData litigationCrawledData;
	// 企业/自然人集合
	private List<LitigationParty> litigationPartyList;

	public LitigationAPIPojo() {

	}

	public LitigationAPIPojo(LitigationCrawledData litigationCrawledData,
			LitigationParsedDataDetail litigationParsedDataDetail, List<LitigationParty> litigationPartyList) {
		this.litigationCrawledData = litigationCrawledData;
		this.litigationParsedDataDetail = litigationParsedDataDetail;
		this.litigationPartyList = litigationPartyList;
	}

	public LitigationParsedDataDetail getLitigationParsedDataDetail() {
		return litigationParsedDataDetail;
	}

	public void setLitigationParsedDataDetail(LitigationParsedDataDetail litigationParsedDataDetail) {
		this.litigationParsedDataDetail = litigationParsedDataDetail;
	}

	public LitigationCrawledData getLitigationCrawledData() {
		return litigationCrawledData;
	}

	public void setLitigationCrawledData(LitigationCrawledData litigationCrawledData) {
		this.litigationCrawledData = litigationCrawledData;
	}

	public List<LitigationParty> getLitigationPartyList() {
		return litigationPartyList;
	}

	public void setLitigationPartyList(List<LitigationParty> litigationPartyList) {
		this.litigationPartyList = litigationPartyList;
	}

	public static void main(String[] args) {
		// String slat="experian_litigation";
		Gson gson = new Gson();
		// LitigationAPIPojo pojo=new LitigationAPIPojo();
		// LitigationParsedDataDetail litigationParsedDataDetail=new
		// LitigationParsedDataDetail();
		// pojo.setLitigationParsedDataDetail(litigationParsedDataDetail);
		// LitigationCrawledData litigationCrawledData=new
		// LitigationCrawledData();
		// pojo.setLitigationCrawledData(litigationCrawledData);
		// List<LitigationParty> list=new ArrayList<LitigationParty>();
		// LitigationParty litigationParty=new LitigationParty();
		// list.add(litigationParty);
		// pojo.setLitigationPartyList(list);
		// List<LitigationAPIPojo> pojos=new ArrayList<LitigationAPIPojo>();
		// pojos.add(pojo);
		// System.out.println(gson.toJson(pojos));
		PartyCategoryAPIPojo api = new PartyCategoryAPIPojo();
		List<LitigationPartyCategory> data = new ArrayList<LitigationPartyCategory>();
		LitigationPartyCategory obj = new LitigationPartyCategory();
		obj.setId(123123);
		obj.setName("testName");
		data.add(obj);
		api.setData(data);
		api.setToken("8cd5b29e0b86315979e224f14780e244");
		System.out.println(gson.toJson(api));
		// System.out.println(DigestUtils.md5DigestAsHex(("optix" + "/" +
		// slat).getBytes()));
		// System.out.println("8cd5b29e0b86315979e224f14780e244".equals(DigestUtils.md5DigestAsHex(("optix"
		// + "/" + slat).getBytes())));
	}

}
