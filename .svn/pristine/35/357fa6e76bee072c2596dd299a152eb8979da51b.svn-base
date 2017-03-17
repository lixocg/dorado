package com.experian.core.database.sharding.shard;

import com.experian.core.database.sharding.builder.ShardObject;

/**
 * Every sharding pojo must implements Strategy 
 * @author bin
 *
 */
public interface Strategy {
	public abstract String getTargetSql(String oldSql,Object parm,ShardObject object);
}	
