package com.eaxperian.test.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eaxperian.test.dao.UserDao;

@Service("tableService")
public class TableServiceImpl implements TableService{
	@Resource
	private UserDao userDao;
	
	public void check(int id){
		String tablePrefix = "user_";
		//1。去redis中看看这个表是否存在
		boolean exist = false;
		//2.不存在就创建一个表
		if(!exist){
			userDao.createTable(tablePrefix+id);
			System.out.println("创建了表:"+(tablePrefix+id));
		}
	}

}
