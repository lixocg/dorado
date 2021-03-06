package com.experian.comp.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.log4j.Logger;
import org.assertj.core.util.Lists;
import org.json.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.experian.comp.CompApplication;
import com.experian.comp.elasticsearch.mapping.MappingHolder;
import com.experian.comp.elasticsearch.mapping.MappingScan;
import com.experian.comp.elasticsearch.param.ESRequest;
import com.experian.comp.elasticsearch.param.ESResponse;
import com.experian.comp.elasticsearch.param.request.AggsParam;
import com.experian.comp.elasticsearch.param.request.BoolParam;
import com.experian.comp.elasticsearch.param.request.Document;
import com.experian.comp.elasticsearch.param.request.SearchParam;
import com.experian.comp.elasticsearch.param.response.SearchResponse;
import com.experian.comp.utility.ElasticSeacrhUtil;
import com.experian.core.utils.GsonUtil;
import com.google.gson.Gson;

import net.minidev.json.JSONObject;

@SpringBootTest(classes = CompApplication.class)
@RunWith(SpringRunner.class)
public class ESTest {
	private static final Logger logger = Logger.getLogger(ESTest.class);
	
	public static Gson gson = new Gson();
	
	@Test
	public void ifIndexExist(){
		boolean e = ElasticSeacrhUtil.ifIndexExist("litigation");
		System.out.println(e);
	}
	
	@Test
	public void createMapping(){
		ESRequest<Class<Litigation>> esRequest = new ESRequest<>();
		esRequest.setIndex("litigation");
		esRequest.setType("detail");
		ESResponse<Void> response = ElasticSeacrhUtil.createMapping(esRequest,Litigation.class);
		System.out.println(gson.toJson(response));
	}

	@Test
	public void testSingleAdd() {
		ESRequest<Document<Litigation>> esRequest = new ESRequest<>();
		Litigation l = new Litigation();
		l.setId("E1");
		l.setCaseNumber("执123");
		l.setSerialCaseNumber("E1");
		l.setCorpName("阿里巴巴");
		l.setSbd("123");
		l.setAmount(100);
		l.setInsertDate(new Date());
		List<Party> parties = Lists.newArrayList();
		Party p1 = new Party();
		p1.setId("E1");
		p1.setName("阿里巴巴");
		p1.setSbd("123");
		p1.setType(1);
		parties.add(p1);

		Party p2 = new Party();
		p2.setId("E2");
		p2.setName("腾讯");
		p2.setSbd("456");
		p2.setType(2);
		parties.add(p2);

		l.setParties(parties);

		esRequest.setIndex("litigation");
		esRequest.setType("detail");
		Document<Litigation> doc = new Document<Litigation>();
		doc.setId(l.getId());
		doc.setDoc(l);
		esRequest.setContent(doc);
		ESResponse<Void> response = ElasticSeacrhUtil.addDoc(esRequest);
		System.out.println(gson.toJson(response));
	}

	@Test
	public void testBulkAdd() {
		ESRequest<List<Document<Litigation>>> esRequest = new ESRequest<>();
		List<Document<Litigation>> docs = Lists.newArrayList();
		for (int i = 2; i < 11; i++) {
			Document<Litigation> doc = new Document<Litigation>();
			Litigation l = new Litigation();
			l.setId("E" + i);
			if (i % 2 == 0) {
				l.setCaseNumber("执456");
			} else {
				l.setCaseNumber("执789");
			}
			if (i < 6) {
				l.setSerialCaseNumber("E2");
			} else {
				l.setSerialCaseNumber("E3");
			}
			if (i < 6) {
				l.setCorpName("Experian中国");
				l.setSbd("1234");
				l.setAmount(100);
			} else if (6 <= i && i < 8) {
				l.setCorpName("搜狗");
				l.setSbd("2345");
				l.setAmount(20);
			} else {
				l.setCorpName("百度");
				l.setSbd("3456");
				l.setAmount(50);
			}
			l.setInsertDate(new Date());
			List<Party> parties = Lists.newArrayList();
			Party p1 = new Party();
			p1.setId("E" + i);
			if (i < 6) {
				p1.setName("Experian中国");
				p1.setSbd("1234");
				p1.setType(1);
			} else if (6 <= i && i < 8) {
				p1.setName("搜狗");
				p1.setSbd("2345");
				p1.setType(2);
			} else {
				p1.setName("百度");
				p1.setSbd("3456");
				p1.setType(1);
			}
			parties.add(p1);

			Party p2 = new Party();
			p2.setId("E100" + i);
			if (i < 6) {
				p2.setName("百度");
				p2.setSbd("3456");
				p2.setType(1);
			} else if (6 <= i && i < 8) {
				p2.setName("Experian中国");
				p2.setSbd("1234");
				p2.setType(1);
			} else {
				p2.setName("搜狗");
				p2.setSbd("2345");
				p2.setType(2);
			}
			parties.add(p2);

			l.setParties(parties);

			doc.setId(l.getId());
			doc.setDoc(l);
			docs.add(doc);
		}

		esRequest.setContent(docs);
		esRequest.setIndex("litigation");
		esRequest.setType("detail");
		ESResponse<Void> response = ElasticSeacrhUtil.addBulkDoc(esRequest);
		System.out.println(gson.toJson(response));
	}

	@Test
	public void testMultiQuery() {

		ESRequest<SearchParam> esRequest = new ESRequest<>();

		SearchParam content = new SearchParam();
		content.setKeyword("E");
		content.setFileds(new String[] { "serialCaseNumber" });

		esRequest.setContent(content);
		esRequest.setIndex("litigation");
		esRequest.setType("detail");
		ESResponse<Litigation> res = ElasticSeacrhUtil.search(esRequest, Litigation.class);
		System.err.println(GsonUtil.toJson(res));
	}

	@Test
	public void testBoolFiler() {
		ESRequest<SearchParam> esRequest = new ESRequest<>();

		SearchParam searcParam = new SearchParam();
		//searcParam.setFileds(new String[] { "serialCaseNumber" });
		List<BoolParam> filters = Lists.newArrayList();
		BoolParam fp1 = new BoolParam();
		fp1.setBoolType((byte) 2);
		fp1.setKey("corpName");
		fp1.setValue("Experian中国");
		filters.add(fp1);

		searcParam.setBools(filters);

		esRequest.setContent(searcParam);
		esRequest.setIndex("litigation");
		esRequest.setType("detail");
		ESResponse<Litigation> res = ElasticSeacrhUtil.search(esRequest, Litigation.class);
		System.err.println(GsonUtil.toJson(res));
	}
	
	@Test
	public void testNestedBoolFiler() {
		ESRequest<SearchParam> esRequest = new ESRequest<>();

		SearchParam searcParam = new SearchParam();
		//searcParam.setFileds(new String[] { "serialCaseNumber" });
		List<BoolParam> filters = Lists.newArrayList();
		BoolParam fp1 = new BoolParam();
		fp1.setBoolType((byte) 2);
		fp1.setKey("name");
		fp1.setValue("Experian中国");
		fp1.setNested(true);
		fp1.setNestedPath("parties");
		filters.add(fp1);

		searcParam.setBools(filters);

		esRequest.setContent(searcParam);
		esRequest.setIndex("litigation");
		esRequest.setType("detail");
		ESResponse<Litigation> res = ElasticSeacrhUtil.search(esRequest, Litigation.class);
		System.err.println(GsonUtil.toJson(res));
	}
	
	@Test
	public void testAggsQuery() {
		ESRequest<SearchParam> esRequest = new ESRequest<>();
		esRequest.setIndex("litigation");
		esRequest.setType("detail");
		SearchParam searchParam = new SearchParam();
		esRequest.setContent(searchParam);

		List<AggsParam> aggs = new ArrayList<>();
		AggsParam agg1 = new AggsParam();
		agg1.setKey("serialCaseNumber");
		aggs.add(agg1);
		searchParam.setAggs(aggs);
		
		ESResponse<Litigation> res = ElasticSeacrhUtil.search(esRequest, Litigation.class);
		logger.info(GsonUtil.toJson(res));
	}
	
	@Test
	public void testScan(){
		new  MappingScan(new String[]{"com.experian.comp.test"});
		System.out.println(MappingHolder.getInstance().getMapping(Litigation.class));
	}
}
