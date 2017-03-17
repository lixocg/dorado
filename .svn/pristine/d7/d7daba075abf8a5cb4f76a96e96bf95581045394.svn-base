package com.experian.comp.redis.config;

import java.util.Set;

import org.assertj.core.util.Sets;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis jedis配置
 * @author lixiongcheng
 *
 */
@Configuration
@PropertySource(value = "classpath:conf/${spring.profiles.active}/redis.properties")
public class RedisConfig implements EnvironmentAware {
	/**
	 * jedis配置信息
	 * @return
	 */
	@Bean
	public JedisPoolConfig jedisPoolConfig() {
		JedisPoolConfig jpc = new JedisPoolConfig();
		jpc.setMaxIdle(Integer.parseInt(env.getProperty("redis.maxTotal")));
		jpc.setMaxIdle(Integer.parseInt(env.getProperty("redis.maxIdle")));
		jpc.setNumTestsPerEvictionRun(Integer.parseInt(env.getProperty("redis.numTestsPerEvictionRun")));
		jpc.setTimeBetweenEvictionRunsMillis(Long.parseLong(env.getProperty("redis.timeBetweenEvictionRunsMillis")));
		jpc.setMinEvictableIdleTimeMillis(Long.parseLong(env.getProperty("redis.minEvictableIdleTimeMillis")));
		jpc.setSoftMinEvictableIdleTimeMillis(Long.parseLong(env.getProperty("redis.softMinEvictableIdleTimeMillis")));
		jpc.setMaxWaitMillis(Long.parseLong(env.getProperty("redis.maxWaitMillis")));
		jpc.setTestOnBorrow(Boolean.parseBoolean(env.getProperty("redis.testOnBorrow")));
		jpc.setTestWhileIdle(Boolean.parseBoolean(env.getProperty("redis.testWhileIdle")));
		jpc.setBlockWhenExhausted(Boolean.parseBoolean(env.getProperty("redis.blockWhenExhausted")));
		return jpc;
	}
	
	/**
	 * 单机版jedis
	 * @param jedisPoolConfig
	 * @return
	 */
	@Bean
	public JedisPool jedisPool(JedisPoolConfig jedisPoolConfig){
		String host = env.getProperty("redis.host");
		int port = Integer.parseInt(env.getProperty("redis.port"));
		JedisPool jp = new JedisPool(jedisPoolConfig,host,port);
		return jp;
	}
	
	/**
	 * 集群版jedis
	 * @param jedisPoolConfig
	 * @return
	 */
	//@Bean
	public JedisCluster jedisCluster(JedisPoolConfig jedisPoolConfig){
		Set<HostAndPort> nodes = Sets.newHashSet();
		HostAndPort node1 = new HostAndPort(env.getProperty("redis.host"), Integer.parseInt(env.getProperty("redis.port")));
		nodes.add(node1);
		JedisCluster jc = new JedisCluster(nodes,jedisPoolConfig);
		return jc;
	}

	private Environment env;

	@Override
	public void setEnvironment(Environment environment) {
		this.env = environment;
	}

}
