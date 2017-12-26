package com.ifox.jdbc.basic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.Test;

public class ResultSetDemo {

	@Test
	public void selectDemo() {
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
//			1.获取连接
			connection = JDBCTools.getConnection();
//			2.获取statement
			statement = (Statement) connection.createStatement();
			String sql = "select * from user";
//			3.执行查询，获取结果集
			rs = statement.executeQuery(sql);
			
			while (rs.next()) {
				int id = rs.getInt(1);
				String name = rs.getString(2);
				System.out.println("id: " + id + ",name: " + name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCTools.release(connection, statement, rs);
		}
		
	}
}
