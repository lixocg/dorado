package com.experian.api;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * API Module ，请求转发，熔断...
 * @author lixiongcheng
 *
 */
@EnableZuulProxy
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@EnableEurekaClient
public class ApiApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(ApiApplication.class).web(true).run(args);
	}

/*	@Bean
	public AccessFilter accessFilter() {
		return new AccessFilter();
	}*/
}
