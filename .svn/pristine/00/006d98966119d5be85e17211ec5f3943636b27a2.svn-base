package com.experian.comp.redis.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.experian.comp.redis.RedisClient;

import redis.clients.jedis.JedisCluster;

/**
 * 集群Redis客户端
 * @author lixiongcheng
 *
 */
//@Component
public class ClusterRedisClient implements RedisClient{

	@Autowired
	private JedisCluster jedisCluster;
	
	@Override
	public String get(String key) {
		return jedisCluster.get(key);
	}

	@Override
	public String set(String key, String value) {
		return jedisCluster.set(key, value);
	}

	@Override
	public String hget(String hkey, String key) {
		return jedisCluster.hget(hkey, key);
	}

	@Override
	public long hset(String hkey, String key, String value) {
		return jedisCluster.hset(hkey, key, value);
	}

	@Override
	public long incr(String key) {
		return jedisCluster.incr(key);
	}

	@Override
	public long expire(String key, int second) {
		return jedisCluster.expire(key, second);
	}

	@Override
	public long ttl(String key) {
		return jedisCluster.ttl(key);
	}

	@Override
	public long hdel(String key, String field) {
		return jedisCluster.hdel(key, field);
	}

	@Override
	public boolean exists(String key) {
		return jedisCluster.exists(key);
	}

}
