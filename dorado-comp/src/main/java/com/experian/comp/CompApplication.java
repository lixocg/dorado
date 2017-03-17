package com.experian.comp;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import com.experian.core.CoreApplication;

/**
 * 此module用于组件封装，比如Rabbitmq，kafka，redis，mongdb，hbase.....
 * 
 * @author lixiongcheng
 *
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@ComponentScan(basePackageClasses = { CoreApplication.class, CompApplication.class })
public class CompApplication {

}
