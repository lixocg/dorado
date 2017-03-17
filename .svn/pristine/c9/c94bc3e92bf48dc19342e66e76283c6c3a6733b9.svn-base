package com.experian.daas.test.baseinfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.experian.daas.baseinfo.BaseInfoApplication;
import com.experian.daas.baseinfo.service.BaseInfoService;

@SpringBootTest(classes = BaseInfoApplication.class)
@RunWith(SpringRunner.class)
public class BaseInfoTest {
	@Autowired
	private BaseInfoService baseInfoService;
	
	@Test
	public void testMatchSbdnum() throws Exception{
		String match = baseInfoService.match("华润(集团)有限公司");
		System.out.println(match);
	}
	
}
