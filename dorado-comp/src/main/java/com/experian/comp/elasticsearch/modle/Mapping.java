package com.experian.comp.elasticsearch.modle;

import java.util.Map;

/**
 * Mapping结构
 * @author lixiongcheng
 *
 */
public class Mapping {
	/**
	 * Mapping原始类
	 */
	private Class<?> rawClass;

	/**
	 * Mapping结构
	 */
	private Map<String, Map<String, Map<String, Map<String, Map<String, Object>>>>> mapping;

	public Map<String, Map<String, Map<String, Map<String, Map<String, Object>>>>> getMapping() {
		return mapping;
	}

	public void setMapping(Map<String, Map<String, Map<String, Map<String, Map<String, Object>>>>> mapping) {
		this.mapping = mapping;
	}

	public Class<?> getRawClass() {
		return rawClass;
	}

	public void setRawClass(Class<?> rawClass) {
		this.rawClass = rawClass;
	}

}
