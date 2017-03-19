package com.experian.comp.elasticsearch.mapping;

import java.util.Map;

import com.experian.comp.elasticsearch.modle.Mapping;
import com.experian.comp.utility.GsonUtil;
import com.google.common.collect.Maps;

/**
 * mapping entity扫描注册对象
 * @author lixiongcheng
 *
 */
public class MappingHolder {
	private static volatile MappingHolder instance;

	public  Map<Class<?>, String> mappingRegister = Maps.newHashMap();

	private MappingHolder() {
	}

	public static MappingHolder getInstance() {
		if (instance == null) {
			synchronized (MappingHolder.class) {
				if (instance == null) {
					instance = new MappingHolder();
				}
			}
		}
		return instance;
	}

	public void register(Mapping mapping) {
		if (mapping != null) {
			mappingRegister.put(mapping.getRawClass(), GsonUtil.toJson(mapping.getMapping()));
		}
	}

	public String getMapping(Class<?> clazz) {
		return this.mappingRegister.get(clazz);
	}
}
