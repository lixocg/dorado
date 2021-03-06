package com.experian.daas.litigation;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.alibaba.druid.support.http.StatViewServlet;
import com.experian.comp.CompApplication;
import com.experian.core.CoreApplication;

/**
 * 诉讼服务
 * @author lixiongcheng
 *
 */
@EnableDiscoveryClient
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@ComponentScan(basePackageClasses = { LitigationApplication.class, CoreApplication.class, CompApplication.class })
public class LitigationApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(LitigationApplication.class).web(true).run(args);
	}

	@Bean
	public ServletRegistrationBean servletRegistrationBean() {
		return new ServletRegistrationBean(new StatViewServlet(), "/druid/*");// ServletName默认值为首字母小写，即myServlet
	}

}
