package com.experian.daas.test.litigation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.experian.comp.rabbitmq.ConsumerHandler;
import com.experian.comp.rabbitmq.RabbitClient;
import com.experian.comp.rabbitmq.StringConsumer;
import com.experian.comp.utility.RabbitUtil;
import com.experian.core.constant.LitiBusiConstant;
import com.experian.daas.litigation.LitigationApplication;
import com.experian.daas.litigation.service.LitigationAdapterService;
import com.google.gson.Gson;

import scala.util.Random;

@SpringBootTest(classes = LitigationApplication.class)
@RunWith(SpringRunner.class)
public class LitigationMqServiceTest {
	@Autowired
	private LitigationAdapterService litigationAdapterService;

	@Autowired
	private RabbitClient rabbit;
	

	public static Gson gson = new Gson();

	// @Test
	// public void xxx() {
	// for (int i = 0; i < 25; i++) {
	// rabbit.deleteQueue("litigationQueue" + i);
	// rabbit.deleteQueue("litigationQueue_" + i);
	// rabbit.deleteQueue("法院ID取模分组_" + i);
	// }
	// rabbit.deleteQueue("matchSbdnumQueue");
	// rabbit.deleteQueue("matchSbdnumQueue_");
	// rabbit.deleteQueue("testQueue");
	// }

	@Test
	public void test01() throws Exception {
		litigationAdapterService.oldDataDeplidate(LitiBusiConstant.DataFetchStatus.UN_FETCH,
				LitiBusiConstant.DataStatus.SUCCESS);
	}

	@Test
	public void lis() throws InterruptedException {
		for (int i = 0; i < 10; i++) {
			rabbit.reveiceMsg("testQueue" + i, new StringConsumer(new ConsumerHandler() {

				@Override
				public void handle(String msg) {
					try {
						Thread.sleep(500);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("rev:" + msg);
				}
			}));
		}
		Thread.sleep(Integer.MAX_VALUE);
	}

	@Test
	public void proce() {
		while (true) {
			int ran = new Random().nextInt(1000);
			int mod = ran % 10;
			System.out.println(ran + "..." + mod);
			rabbit.sendMsg("testQueue" + mod, ran + "..." + mod);
		}
	}
	
	@Test
	public void delQueue(){
		for(int i=0;i<10000;i++){
			rabbit.deleteQueue("litigationQueue"+i);
			rabbit.deleteQueue("testQueue"+i);
		}
		rabbit.deleteQueue("matchSbdnumQueue");
	}
	
	public String testQueue="testQueue";
	
	@Test
	public void testRabbitRec(){
		for(int i=0;i<5;i++){
			RabbitUtil.reveiceMsg(testQueue+i, new StringConsumer(new ConsumerHandler() {
				
				@Override
				public void handle(String msg) {
					try {
						//Thread.sleep(200);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println(msg);
				}
			}));
		}
		
		try {
			Thread.sleep(Integer.MAX_VALUE);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testRabbitSend(){
		long s = System.currentTimeMillis();
		System.out.println("send 开始");
		for(int i=0;i<100000;i++){
			int j = new Random().nextInt(5);
			RabbitUtil.sendMsg(testQueue+j, String.format(testQueue+j+":消息%s", i));
		}
		System.out.println(String.format("send 结束，耗时:%s", (System.currentTimeMillis()-s)));
	}
	
}
