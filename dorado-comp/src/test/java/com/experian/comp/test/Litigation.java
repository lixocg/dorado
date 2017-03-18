package com.experian.comp.test;

import java.util.Date;
import java.util.List;

import com.experian.comp.elasticsearch.annotation.Document;
import com.experian.comp.elasticsearch.annotation.Field;
import com.experian.comp.elasticsearch.annotation.FieldIndex;
import com.experian.comp.elasticsearch.enums.FieldType;

@Document(indexName="litigiation",type="detail")
public class Litigation {
	@Field(type = FieldType.String,index = FieldIndex.not_analyzed, store = true)
	private String id;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
	private String caseNumber;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
	private String serialCaseNumber;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
	private String corpName;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
	private String sbd;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
	private float amount;
	
	@Field(type = FieldType.Date, index = FieldIndex.not_analyzed,store = true)
	private Date insertDate;
	
	@Field(type = FieldType.Nested, index = FieldIndex.not_analyzed,store = true)
	private List<Party> parties;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCaseNumber() {
		return caseNumber;
	}

	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}

	public String getSerialCaseNumber() {
		return serialCaseNumber;
	}

	public void setSerialCaseNumber(String serialCaseNumber) {
		this.serialCaseNumber = serialCaseNumber;
	}

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	public String getSbd() {
		return sbd;
	}

	public void setSbd(String sbd) {
		this.sbd = sbd;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public Date getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	public List<Party> getParties() {
		return parties;
	}

	public void setParties(List<Party> parties) {
		this.parties = parties;
	}

}
