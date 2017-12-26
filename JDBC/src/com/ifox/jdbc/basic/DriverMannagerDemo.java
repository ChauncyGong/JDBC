package com.ifox.jdbc.basic;

import java.io.InputStream;
import java.sql.DriverManager;
import java.util.Properties;

import org.junit.Test;

import com.mysql.jdbc.Connection;

public class DriverMannagerDemo {

	@Test
	public void testDriverManager() throws Exception {
		String driverClass = null;
		String url = null;
		String user = null;
		String password = null;
		
		/**
		 * 1.加载属性文件
		 */
		InputStream in = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("db.properties");
		Properties properties = new Properties();
		properties.load(in);
		
		/**
		 * 2.获取连接属性值
		 */
		driverClass = properties.getProperty("driverClass");
		url = properties.getProperty("url");
		user = properties.getProperty("user");
		password = properties.getProperty("password");
		System.out.println(driverClass + " " + url + " " + user + " " +password);
		
		/**
		 * 3.加载驱动,底层会通过DriverManager.registerDriver方法注册此驱动
		 *  static {
	     * 		try {
	     *     		java.sql.DriverManager.registerDriver(new Driver());
	     *   	} catch (SQLException E) {
	     *       	throw new RuntimeException("Can't register driver!");
	     *   	}
	     *	}
		 */
		Class.forName(driverClass);
		
		/**
		 * 4.使用DriverManager获取连接
		 */
		Connection connection = (Connection) DriverManager.getConnection(url, user, password);
		System.out.println(connection);
	}
}
