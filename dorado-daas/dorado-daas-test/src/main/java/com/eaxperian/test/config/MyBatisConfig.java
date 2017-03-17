package com.eaxperian.test.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.experian.core.database.sharding.builder.ShardScanner;
import com.experian.core.database.sharding.plugin.ShardPlugin;
import com.experian.core.database.sharding.sqlsessiontemplate.CustomSqlSessionTemplate;
import com.experian.core.database.sharding.sqlsessiontemplate.CustomerContextHolder;
import com.experian.core.database.tx.ChainedTransactionManager;
import com.github.pagehelper.PageHelper;

/**
 * mybatis配置
 * 
 * @author lixiongcheng
 *
 */
@Configuration
@EnableTransactionManagement
@PropertySource(value="classpath:conf/${spring.profiles.active}/db.properties")
public class MyBatisConfig implements EnvironmentAware{
	public Environment env;


	// >>>>>>>>>>>>>>>>>>>>>第三个数据源结束<<<<<<<<<<<<<<<<<<<<

	// >>>>>>>>>>>>>>>>>>>>>第四个数据源开始<<<<postgresql库<<<<<<<<<<<<<<<<
	/**
	 * 第四个数据源
	 * 
	 * @return
	 * @throws Exception
	 */
	@Bean
	@Primary
	DataSource db4DataSource() throws Exception {
		Properties props = new Properties();
		props.put("driverClassName", env.getProperty("db1.driverClassName"));
		props.put("url", env.getProperty("db1.url"));
		props.put("username", env.getProperty("db1.username"));
		props.put("password", env.getProperty("db1.password"));
		props.put("initialSize", env.getProperty("db1.initialSize"));
		props.put("minIdle", env.getProperty("db1.minIdle"));
		props.put("maxActive", env.getProperty("db1.maxActive"));
		props.put("maxWait", env.getProperty("db1.maxWait"));
		props.put("timeBetweenEvictionRunsMillis", env.getProperty("db1.timeBetweenEvictionRunsMillis"));
		props.put("minEvictableIdleTimeMillis", env.getProperty("db1.minEvictableIdleTimeMillis"));
		props.put("validationQuery", env.getProperty("db1.validationQuery"));
		props.put("testWhileIdle", env.getProperty("db1.testWhileIdle"));
		props.put("testOnBorrow", env.getProperty("db1.testOnBorrow"));
		props.put("filters", env.getProperty("db1.filters"));
		return DruidDataSourceFactory.createDataSource(props);
	}

	@Bean
	public SqlSessionFactoryBean db4SqlSessionFactoryBean(@Qualifier("db4DataSource") DataSource dataSource)
			throws Exception {
		SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
		fb.setDataSource(dataSource);
		fb.setMapperLocations(
				new PathMatchingResourcePatternResolver().getResources("classpath*:com/eaxperian/**/dao/xml/*.xml"));
		fb.setConfigLocation(
				new PathMatchingResourcePatternResolver().getResource("classpath:mybatis/mybatisConfig.xml"));
		fb.setPlugins(new Interceptor[] { pageHelper(), new ShardPlugin() });
		String[] packagesToScan={"com.eaxperian.test.dao"};
		new ShardScanner(packagesToScan);
		return fb;
	}

	@Bean
	public DataSourceTransactionManager db4TransactionManager(@Qualifier("db4DataSource") DataSource dataSource)
			throws Exception {
		return new DataSourceTransactionManager(dataSource);
	}

	// >>>>>>>>>>>>>>>>>>>>>第四个数据源结束<<<<<<<<<<<<<<<<<<<<

	/**
	 * 多数据源事物管理器
	 * 
	 * @param dtm1
	 * @param dtm2
	 * @return
	 */
	@Bean
	@Primary
	public ChainedTransactionManager chainedTransactionManager(
			@Qualifier("db4TransactionManager") DataSourceTransactionManager dtm4) {
		return new ChainedTransactionManager( dtm4);
	}

	/**
	 * customSqlSessionTemplate 配置
	 * 
	 * @param sfb1
	 * @param sfb2
	 * @return
	 * @throws Exception
	 */
	@Bean(name = "customSqlSessionTemplate")
	public CustomSqlSessionTemplate customSqlSessionTemplate(
			@Qualifier("db4SqlSessionFactoryBean") SqlSessionFactoryBean sfb4) throws Exception {
		CustomSqlSessionTemplate cst = new CustomSqlSessionTemplate(sfb4.getObject());
		Map<Object, SqlSessionFactory> targetSqlSessionFactorys = new HashMap<Object, SqlSessionFactory>();
		targetSqlSessionFactorys.put(CustomerContextHolder.MYSQL, sfb4.getObject());
		cst.setTargetSqlSessionFactorys(targetSqlSessionFactorys);
		cst.setDefaultTargetSqlSessionFactory(sfb4.getObject());
		return cst;
	}

	/**
	 * mybatis Dao扫描配置
	 * 
	 * @param customSqlSessionTemplate
	 * @return
	 */
	@Bean
	public MapperScannerConfigurer mapperScannerConfigurer(CustomSqlSessionTemplate customSqlSessionTemplate) {
		MapperScannerConfigurer msc = new MapperScannerConfigurer();
		msc.setBasePackage("com.eaxperian.**.dao");
		msc.setSqlSessionTemplateBeanName("customSqlSessionTemplate");
		return msc;
	}

	/**
	 * pageHelper 插件
	 * 
	 * @return
	 */
	private PageHelper pageHelper() {
		PageHelper pageHelper = new PageHelper();
//		Properties properties = new Properties();
//		properties.setProperty("reasonable", "true");
//		properties.setProperty("supportMethodsArguments", "true");
//		properties.setProperty("returnPageInfo", "check");
//		properties.setProperty("params", "count=countSql");
//		properties.setProperty("dialect", "mysql");
//		pageHelper.setProperties(properties);
		return pageHelper;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.env = environment;
	}

}