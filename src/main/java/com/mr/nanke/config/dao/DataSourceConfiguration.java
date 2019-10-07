package com.mr.nanke.config.dao;

import java.beans.PropertyVetoException;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mr.nanke.utils.DESUtils;


/***
 * 配置DataSource到IOC容器中
 * @author 夏小颜
 *
 */
@Configuration
@MapperScan("com.mr.nanke.dao")
public class DataSourceConfiguration {
	//通过@Value可从properties文件中读取
	@Value("${jdbc.driver}")
	private String jdbcDriver;
	@Value("${jdbc.url}")
	private String jdbcUrl;
	@Value("${jdbc.username}")
	private String jdbcUsername;
	@Value("${jdbc.password}")
	private String jdbcPassword;
	
	/***
	 * 生成spring-dao.xml对应的bean
	 * @throws PropertyVetoException 
	 * 
	 */
	
	@Bean(name="dataSource")
	public ComboPooledDataSource createDateSource() throws PropertyVetoException {
		//生成dataSource实例
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		dataSource.setDriverClass(jdbcDriver);
		dataSource.setJdbcUrl(jdbcUrl);
		dataSource.setUser(DESUtils.getDecryptString(jdbcUsername));
		dataSource.setPassword(DESUtils.getDecryptString(jdbcPassword));
		dataSource.setMaxPoolSize(30);
		dataSource.setMinPoolSize(10);
		dataSource.setAutoCommitOnClose(false);
		dataSource.setCheckoutTimeout(10000);
		dataSource.setAcquireRetryAttempts(2);
		return dataSource;
	}
	
	
	
}
