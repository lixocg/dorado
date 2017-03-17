package com.experian.core.database.sharding.builder;

import com.experian.core.database.sharding.shard.Strategy;

public class ShardParser {
	private static final ShardParser instance = new ShardParser();

	public static ShardParser getInstance() {
		return instance;
	}

	public String parse(String oldSql, Object parm, ShardObject shardObject) {
		if (shardObject.getStrategy() != null) {
			try {
				Strategy strategy = (Strategy) shardObject.getStrategy().newInstance();
				return strategy.getTargetSql(oldSql, parm, shardObject);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
