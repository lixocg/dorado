package com.experian.comp.utility;

import com.experian.comp.redis.RedisClient;
import com.experian.comp.redis.impl.SingleRedisClient;
import com.experian.core.utils.SpringContextUtil;

/**
 * redis 工具
 * 
 * @author lixiongcheng
 *
 */
public class RedisUtil {
	private RedisUtil() {
	}

	public static volatile RedisClient redis;

	public static RedisClient getRedis() {
		if (redis == null) {
			synchronized (RedisClient.class) {
				if (redis == null) {
					redis = (SingleRedisClient) SpringContextUtil.getBean("singleRedisClient");
				}
			}
		}
		return redis;
	}

	public static String get(String key) {
		return getRedis().get(key);
	}

	public static String set(String key, String value) {
		return getRedis().set(key, value);
	}

	public static String hget(String hkey, String key) {
		return getRedis().hget(hkey, key);
	}

	public static long hset(String hkey, String key, String value) {
		return getRedis().hset(hkey, key, value);
	}

	public static long incr(String key) {
		return getRedis().incr(key);
	}

	public static long expire(String key, int second) {
		return getRedis().expire(key, second);
	}

	public static long ttl(String key) {
		return getRedis().ttl(key);
	}

	public static long hdel(String iNDEX_CONTENT_REDIS_KEY, String string) {
		return getRedis().hdel(iNDEX_CONTENT_REDIS_KEY, string);
	}

	public static boolean exists(String key) {
		return getRedis().exists(key);
	}
}
