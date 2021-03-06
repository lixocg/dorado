package com.experian.comp.rabbitmq.config;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

//@Configuration
@PropertySource(value = "classpath:conf/${spring.profiles.active}/rabbitmq.properties")
public class RabbitMqConfig implements EnvironmentAware {
	@Bean
	public ConnectionFactory connectionFactory() {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(env.getProperty("rabbit.host"));
		factory.setUsername(env.getProperty("rabbit.username"));
		factory.setPassword(env.getProperty("rabbit.password"));
		return factory;
	}

	@Bean
	public Connection connection(ConnectionFactory connectionFactory) throws Exception {
		Connection connection = connectionFactory.newConnection();
		return connection;
	}

	private Environment env;

	@Override
	public void setEnvironment(Environment environment) {
		this.env = environment;
	}

}
