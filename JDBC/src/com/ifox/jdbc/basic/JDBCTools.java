package com.ifox.jdbc.basic;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCTools {
	
	public static Connection getConnection() throws Exception {
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
		
		/**
		 * 3.加载驱动,底层会通过DriverManager.registerDriver方法注册此驱动
		 */
		Class.forName(driverClass);
		
		/**
		 * 4.使用DriverManager获取连接
		 */
		return (Connection) DriverManager.getConnection(url, user, password);
	}
	
	public static void release(Connection connection, Statement statement) {
		release(connection, statement, null);
	}
	
	public static void release(Connection connection, Statement statement, ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
