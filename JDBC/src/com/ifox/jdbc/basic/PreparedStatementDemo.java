package com.ifox.jdbc.basic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import org.junit.Test;

public class PreparedStatementDemo {
	
	@Test
	public void testPreparedStatement() throws Exception {
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		String sql = "SELECT * FROM student WHERE id = ?";
		try {
			connection = JDBCTools.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setObject(1, 1);
			ResultSet rs = preparedStatement.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			System.out.println(columnCount);
			while (rs.next()) {
				System.out.println(rs.getObject(1));
				System.out.println(rs.getObject(2));
				System.out.println(rs.getObject(3));
				System.out.println(rs.getObject(4));
				System.out.println(rs.getObject(5));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.release(connection, preparedStatement);
		}
	}
}
