package com.experian.daas.litigation.jobs;

import java.util.concurrent.ExecutorService;

import org.apache.log4j.Logger;

public class ThreadPoolLog extends Thread {
	Logger logger=Logger.getLogger(ThreadPoolLog.class);

	private final String name;
	private final ExecutorService executorService;

	public ThreadPoolLog(String name, ExecutorService executorService) {
		this.executorService = executorService;
		this.name = name;
	}

	@Override
	public void run() {
		long start=System.currentTimeMillis();
		try {
			executorService.shutdown();
			while (true) {
				if (executorService.isTerminated()) {
					long end=System.currentTimeMillis();
					logger.info("任务线程池《"+name+"》执行完毕！耗时:<"+(end-start)/1000+">");
					break;
				}
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}