package com.experian.comp.redis;

public interface RedisClient {
	String get(String key);

	String set(String key, String value);

	String hget(String hkey, String key);

	long hset(String hkey, String key, String value);

	long incr(String key);

	long expire(String key, int second);

	long ttl(String key);

	long hdel(String iNDEX_CONTENT_REDIS_KEY, String string);

	boolean exists(String key);
}
