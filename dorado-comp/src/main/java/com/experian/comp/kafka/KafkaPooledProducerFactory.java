package com.experian.comp.kafka;

import java.util.Properties;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;

public class KafkaPooledProducerFactory extends BasePooledObjectFactory<Producer<String, String>> {
	private Properties properties;

	public KafkaPooledProducerFactory(Properties properties) {
		this.properties = properties;
	}

	@Override
	public Producer<String, String> create() throws Exception {
		return new KafkaProducer<String, String>(properties);
	}

	@Override
	public PooledObject<Producer<String, String>> wrap(Producer<String, String> kafkaProducer) {
		return new DefaultPooledObject<Producer<String, String>>(kafkaProducer);
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
}
