package com.experian.comp.test;

import java.util.Date;
import java.util.List;

public class Litigation {
	private String id;
	private String caseNumber;
	private String serialCaseNumber;
	private String corpName;
	private String sbd;
	private float amount;
	private Date insertDate;
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
