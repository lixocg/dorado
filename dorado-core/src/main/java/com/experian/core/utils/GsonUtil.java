package com.experian.core.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {
	private static final Gson gson = new GsonBuilder() // null 也序列化
			.setDateFormat("yyyy-MM-dd HH:mm:ss") // 时间转化为特定格式 yyyy-MM-dd
													// HH:mm:ss
			.create();

	private GsonUtil() {
	}

	public static String toJson(Object obj) {
		return gson.toJson(obj);
	}

	public static <T> T fronJson(String json, Class<T> clazz) {
		return gson.fromJson(json, clazz);
	}

	/**
	 * 泛型对象
	 * 
	 * @param json
	 *            json数据
	 * @param raw
	 *            原始数据类型
	 * @param args
	 *            泛型类型
	 * @return
	 */
	public static <T> T fromJson(String json, final Class<?> raw, final Type... args) {
		return gson.fromJson(json, new ParameterizedType() {

			public Type getRawType() {
				return raw;
			}

			public Type[] getActualTypeArguments() {
				return args;
			}

			public Type getOwnerType() {
				return null;
			}
		});
	}
}
