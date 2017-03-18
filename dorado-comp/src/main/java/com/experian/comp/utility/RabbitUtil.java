package com.experian.comp.utility;

import com.experian.comp.rabbitmq.RabbitClient;
import com.experian.comp.rabbitmq.StringConsumer;
import com.experian.core.utils.SpringContextUtil;

/**
 * rabbitmq 工具类
 * 
 * @author lixiongcheng
 *
 */
public class RabbitUtil {
	private RabbitUtil() {
	}

	private static volatile RabbitClient rabbit;

	public static RabbitClient getRabbit(){
		if(rabbit==null){
			synchronized (RabbitClient.class) {
				if(rabbit==null){
					rabbit = (RabbitClient)SpringContextUtil.getBean("rabbitClient");
				}
			}
		}
		return rabbit;
	}

	/**
	 * 发送消息
	 * 
	 * @param queueName
	 *            队列名称
	 * @param msg
	 *            消息
	 */
	public static void sendMsg(String queueName, String msg) {
		getRabbit().sendMsg(queueName, msg);
	}

	/**
	 * 接受消息
	 * 
	 * @param queueName
	 *            队列名称
	 * @param consumer
	 *            处理字符串消费者
	 */
	public static void reveiceMsg(String queueName, StringConsumer consumer) {
		getRabbit().reveiceMsg(queueName, consumer);
	}
}
