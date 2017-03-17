package com.experian.comp.kafka;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

public class PooledKafka implements InitializingBean {
	private static final Logger logger = Logger.getLogger(PooledKafka.class);

	private GenericObjectPool<Producer<String, String>> pool;
	private Resource configLocation;
	private Properties config;
	private Producer<String, String> producer;

	public PooledKafka() {
	}

	public void initPooled(Properties config) {
		synchronized (this) {
			if (this.pool == null) {
				this.config = config;
				pool = new GenericObjectPool<Producer<String, String>>(new KafkaPooledProducerFactory(config));
				pool.setMaxIdle(
						config.containsKey("pool.maxIdle") ? Integer.parseInt(config.getProperty("pool.maxIdle")) : 10);
				pool.setMinIdle(
						config.containsKey("pool.minIdle") ? Integer.parseInt(config.getProperty("pool.minIdle")) : 3);
				pool.setMaxTotal(config.containsKey("pool.maxTotal")
						? Integer.parseInt(config.getProperty("pool.maxTotal")) : 500);
				pool.setMaxWaitMillis(config.containsKey("pool.maxWaitMillis")
						? Integer.parseInt(config.getProperty("pool.maxWaitMillis")) : 100000);
			}
		}
	}

	public boolean send(String topic, String key, String value) {
		ProducerRecord<String, String> message = new ProducerRecord<String, String>(topic, key, value);
		if (pool != null) {
			try {
				producer = pool.borrowObject();
				producer.send(message);
			} catch (Exception e) {
				logger.error(e);
			} finally {
				try {
					pool.returnObject(producer);
				} catch (Exception e) {
					logger.error(e);
				}
			}
			return true;
		}
		return false;
	}

	public boolean send(String topic, String value) {
		return send(topic, null, value);
	}

	public void receive(ConsumerCallback callback, String groupId, String topic, Integer threadNum) {
		this.config.setProperty("group.id", groupId);
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(this.config);
		if (threadNum == null) {
			threadNum = 1;
		}
		consumer.subscribe(Arrays.asList(topic));

		ExecutorService executor = Executors.newFixedThreadPool(threadNum);
		try {
			while (true) {
				ConsumerRecords<String, String> records = consumer.poll(200);
				for (final ConsumerRecord<String, String> record : records) {
					executor.submit(new ConsumerTask(record, callback));
				}
			}
		} catch (WakeupException e) {
			// ignore for shutdown
		} finally {
			consumer.close();
		}
	}

	public Resource getConfigLocation() {
		return configLocation;
	}

	public void setConfigLocation(Resource configLocation) {
		this.configLocation = configLocation;
	}

	public void afterPropertiesSet() throws Exception {
		try {
			Properties config = new Properties();
			config.load(configLocation.getInputStream());
			initPooled(config);
		} catch (IOException e) {
			logger.error("Properties Load Error !\n", e);
		}
	}
}
