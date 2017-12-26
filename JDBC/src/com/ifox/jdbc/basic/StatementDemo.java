package com.ifox.jdbc.basic;

import java.io.InputStream;
import java.sql.DriverManager;
import java.util.Properties;

import org.junit.Test;

import java.sql.Connection;
import java.sql.Statement;

public class StatementDemo {

	@Test
	public void testStatement() throws Exception {
		
		InputStream in = Thread.currentThread().getContextClassLoader()
							.getResourceAsStream("db.properties");
		Properties properties = new Properties();
		properties.load(in);
		
		String driverClass = properties.getProperty("driverClass");
		String url = properties.getProperty("url");
		String user = properties.getProperty("user");
		String password = properties.getProperty("password");
		
		Class.forName(driverClass);
		
		Connection connection = null;
		Statement statement = null;
		
		try {
			//1.获取连接对象
			connection =(Connection) DriverManager.getConnection(url, user, password);
			//2.创建Statement
			statement = (Statement) connection.createStatement(); 
			
			//3.执行sql语句
//			String sql = "insert into user(name) values('aha')";
//			String sql2 = "update user set name = 'updatedName' where id = 1";
			String sql3 = "delete from user where id = 1";
			statement.execute(sql3);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			
			try {
				if (statement != null) {
					//4.关闭Statement对象
					statement.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}finally {
				try {
					if(connection != null) {
						//5.关闭连接
						connection.close();
					}
				} catch (Exception e3) {
					e3.printStackTrace();
				}
			}
		}
	}
}
