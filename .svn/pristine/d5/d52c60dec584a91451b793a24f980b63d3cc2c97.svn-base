package org.mybatis.generator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

public class GeneratorSqlmap {

	public static void generator() throws Exception{

		List<String> warnings = new ArrayList<String>();
		Properties prop=new Properties();
		prop.load(Object.class.getResourceAsStream("/generatorConfig.properties"));  
		boolean overwrite = true;
		//指定 逆向工程配置文件
		File configFile = new File(System.getProperty("user.dir")+"/src/main/resources/generatorConfig.xml"); 
		ConfigurationParser cp = new ConfigurationParser(prop,warnings);
		Configuration config = cp.parseConfiguration(configFile);
		DefaultShellCallback callback = new DefaultShellCallback(overwrite);
		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config,callback, warnings);
		myBatisGenerator.generate(null);
		System.out.println("completed....");
	} 
	public static void main(String[] args) throws Exception {
		try {
			GeneratorSqlmap.generator();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}