package com.experian.comp.elasticsearch.param.request;

/**
 * bool操作入参数
 * 
 * @author lixiongcheng
 *
 */
public class BoolParam {
	private String key;
	private Object value;
	private byte type = 1;// 过滤类型，1-mathc（根据分词），2-term（完全匹配）,3-multi_match,4-range

	private int multiMatchFlag = 1;// type为3时使用

	// type为4时使用
	private byte rangeType;// 1:大于等于-小于等于，2：大于等于-小于,3：大于-小于等于,4:大于-小于
	private Object maxValue;
	private Object minValue;

	private boolean isDate = false;// 是否为日期匹配
	private String format;// 日期格式

	private boolean isNested;// 是否是内嵌
	private String nestedPath;// 假如是内嵌,需要内嵌path

	private byte boolType = 1;// 1:must,2:filter,3:should

	private byte minimumShouldMatch = 1;// should查询时候使用

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public int getMultiMatchFlag() {
		return multiMatchFlag;
	}

	public void setMultiMatchFlag(int multiMatchFlag) {
		this.multiMatchFlag = multiMatchFlag;
	}

	public byte getRangeType() {
		return rangeType;
	}

	public void setRangeType(byte rangeType) {
		this.rangeType = rangeType;
	}

	public Object getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Object maxValue) {
		this.maxValue = maxValue;
	}

	public Object getMinValue() {
		return minValue;
	}

	public void setMinValue(Object minValue) {
		this.minValue = minValue;
	}

	public boolean isDate() {
		return isDate;
	}

	public void setDate(boolean isDate) {
		this.isDate = isDate;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public boolean isNested() {
		return isNested;
	}

	public void setNested(boolean isNested) {
		this.isNested = isNested;
	}

	public String getNestedPath() {
		return nestedPath;
	}

	public void setNestedPath(String nestedPath) {
		this.nestedPath = nestedPath;
	}

	public byte getBoolType() {
		return boolType;
	}

	public void setBoolType(byte boolType) {
		this.boolType = boolType;
	}

	public byte getMinimumShouldMatch() {
		return minimumShouldMatch;
	}

	public void setMinimumShouldMatch(byte minimumShouldMatch) {
		this.minimumShouldMatch = minimumShouldMatch;
	}

}
