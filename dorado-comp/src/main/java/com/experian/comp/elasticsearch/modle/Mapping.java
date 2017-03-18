package com.experian.comp.elasticsearch.modle;

import java.util.Map;

import com.experian.comp.utility.GsonUtil;
import com.google.common.collect.Maps;

public class Mapping {
	private Map<String, Map<String, Map<String, Map<String, Map<String, Object>>>>> mapping;

	public Map<String, Map<String, Map<String, Map<String, Map<String, Object>>>>> getMapping() {
		return mapping;
	}

	public void setMapping(Map<String, Map<String, Map<String, Map<String, Map<String, Object>>>>> mapping) {
		this.mapping = mapping;
	}

	public static void main(String[] args) {
		Map<String, Map<String, Map<String, Map<String, Map<String, Object>>>>> m = Maps.newHashMap();
		Map<String, Map<String, Map<String, Map<String, Object>>>> value = Maps.newHashMap();
		Map<String, Map<String, Map<String, Object>>> v1 = Maps.newHashMap();
		Map<String, Map<String, Object>> v2 = Maps.newHashMap();
		Map<String, Object> v3 = Maps.newHashMap();
		v3.put("type", "float");
		v3.put("fields", "sss");
		v2.put("amount", v3);
		v1.put("properties", v2);
		value.put("detail", v1);
		m.put("mappings", value);

		System.out.println(GsonUtil.toJson(m));
	}
}
