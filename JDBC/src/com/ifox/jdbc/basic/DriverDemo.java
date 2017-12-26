package com.ifox.jdbc.basic;

import java.io.InputStream;
import java.sql.Driver;
import java.util.Properties;

import org.junit.Test;

import java.sql.Connection;

/**
 * boolean acceptsURL(String url) 查询驱动程序是否认为它可以打开到给定 URL 的连接。 
 * Connection connect(String url, Properties info) 试图创建一个到给定 URL 的数据库连接。 
 * int getMajorVersion()  获取此驱动程序的主版本号。 
 * int getMinorVersion()  获得此驱动程序的次版本号。 
 * DriverPropertyInfo[] getPropertyInfo(String url, Properties info) 获得此驱动程序的可能属性信息。 
 * boolean jdbcCompliant() 报告此驱动程序是否是一个真正的 JDBC CompliantTM 驱动程序。 
 */
public class DriverDemo {
	
	/**
	 * Driver是一个接口，由数据库厂商提供实现
	 * 1.加入mysql驱动
	 * 	
	 * @throws ClassNotFoundException 
	 */
	@Test
	public void testDriver() throws Exception {
		//1.创建Driver对象
		Driver driver = new com.mysql.jdbc.Driver();
		
		//2.准备数据库的基本信息
		String url = "jdbc:mysql://localhost:3306/demo";
		Properties properties = new Properties();
		properties.put("user", "root");
		properties.put("password", "admin");
		
		//3.调用Driver接口的connect(url, info)获取数据库连接
		Connection connection = (Connection) driver.connect(url, properties);
		System.out.println(connection);
	}
	
	
	@Test
	public void getConnection() throws Exception {
		//1.获取资源文件
		InputStream in = getClass().getClassLoader().getResourceAsStream("db.properties");
		Properties properties = new Properties();
		properties.load(in);
		
		//2.得到连接驱动和连接地址
		String driverClass = properties.getProperty("driverClass");
		String url = properties.getProperty("url");
		
		//3.获取连接
		Driver driver = (Driver) Class.forName(driverClass).newInstance();
		Connection connection = (Connection) driver.connect(url, properties);
		System.out.println(connection);
		
	}
}
