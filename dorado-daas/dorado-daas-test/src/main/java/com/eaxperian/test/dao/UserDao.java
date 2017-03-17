package com.eaxperian.test.dao;

import com.eaxperian.test.IdStrategy;
import com.eaxperian.test.modle.User;
import com.experian.core.database.sharding.annotation.Params;
import com.experian.core.database.sharding.annotation.Shard;

import java.util.List;

import org.apache.ibatis.annotations.Param;

@Shard
public interface UserDao {
    int deleteByPrimaryKey(Integer id);

    @Params(tables={"user"},strategy=IdStrategy.class)
    int insert(User record);

    @Params(tables={"user"},strategy=IdStrategy.class)
    User selectByPrimaryKey(Integer id);

    List<User> selectAll();

    @Params(tables={"user"},strategy=IdStrategy.class)
    int updateByPrimaryKey(User record);
    
    void createTable(@Param("name")String name);
    
    int ifTableExists(@Param("tableName")String tableName,@Param("tableSchema")String tableSchema);
}