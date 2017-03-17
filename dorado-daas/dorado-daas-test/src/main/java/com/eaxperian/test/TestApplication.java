package com.eaxperian.test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.alibaba.druid.support.http.StatViewServlet;
import com.experian.core.CoreApplication;



@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@ComponentScan(basePackageClasses = { TestApplication.class, CoreApplication.class })
public class TestApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(TestApplication.class).web(true).run(args);
	}

	@Bean
	public ServletRegistrationBean servletRegistrationBean() {
		return new ServletRegistrationBean(new StatViewServlet(), "/druid/*");// ServletName默认值为首字母小写，即myServlet
	}

}
