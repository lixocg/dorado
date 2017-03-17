package com.experian.test;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.eaxperian.test.TestApplication;
import com.eaxperian.test.dao.UserDao;
import com.eaxperian.test.modle.User;

@SpringBootTest(classes = TestApplication.class)
@RunWith(SpringRunner.class)
public class Testt {
	@Resource
	private UserDao userDao;
	
	@Test
	public void t1(){
		User record = new User();
		record.setId(10);;
		record.setName("ss");
		record.setBirth(new Date());
		userDao.insert(record );
	}
	
	@Test
	public void t2(){
		User u1 = userDao.selectByPrimaryKey(2);
		User u2 = userDao.selectByPrimaryKey(5);
		User u3 = userDao.selectByPrimaryKey(3);
		System.out.println(u1.getId());
		System.out.println(u2.getId());
		System.out.println(u3.getId());
	}
	
	@Test
	public void t3(){
		User record = new User();
		record.setId(10);
		record.setBirth(new Date());
		record.setName("ssss");
		userDao.updateByPrimaryKey(record );
	}
	
	@Test
	public void t4(){
		userDao.createTable("user_20");
	}
	
	@Test
	public void t5(){
		System.out.println(userDao.ifTableExists("user", "daas1"));
	}
}
