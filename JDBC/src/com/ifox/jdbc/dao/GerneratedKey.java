package com.ifox.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.Test;


public class GerneratedKey {
	
	/**
	 * 获取自增主键值
	 */
	@Test
	public void test() {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		String sql = "insert into student(name,id_card,grade) values('jims','1212',20)";
		
		try {
			connection = JDBCUtils.getConnection();
			PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.executeUpdate();
			/**
			 * 获取插入的主键
			 */
			resultSet = ps.getGeneratedKeys();
			while (resultSet.next()) {
				System.out.println(resultSet.getObject(1));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.release(connection, statement, null);
		}
	}
}
