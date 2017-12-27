package com.ifox.jdbc.advance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import org.junit.Test;

import com.ifox.jdbc.dao.JDBCUtils;

public class BatchDemo {

	@Test
	public void testStatement() {
		Connection con = null;
		Statement statement = null;
		String sql = null;
		try {
			long start = System.currentTimeMillis();
			con = JDBCUtils.getConnection();
			con.setAutoCommit(false);
			statement = con.createStatement();
			for (int i = 0; i < 100000; i++) {
				sql = "insert into person(name,email,age) values('person" 
						+ i +"','one" + i + "@126.com','" + i % 20 + "')";
				statement.execute(sql);
			}
			con.commit();
			long end = System.currentTimeMillis();
			System.out.println(end - start);		//22523
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.release(con, statement);
		}
	}
	
	@Test
	public void testPreparedStatement() {
		Connection con = null;
		PreparedStatement ps = null;
		String sql = "insert into person(name,email,age) values(?, ?, ?)";
		try {
			long start = System.currentTimeMillis();
			con = JDBCUtils.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement(sql);
			for (int i = 0; i < 100000; i++) {
				ps.setString(1, "person" + i);
				ps.setString(2, "one" + i + "@126.com");
				ps.setInt(3, i % 20);
				ps.executeUpdate();
			}
			con.commit();
			long end = System.currentTimeMillis();
			System.out.println(end - start);		//22942
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.release(con, ps);
		}
	}
	
	@Test
	public void testPreparedStatementBatch() {
		Connection con = null;
		PreparedStatement ps = null;
		String sql = "insert into person(name,email,age) values(?, ?, ?)";
		try {
			long start = System.currentTimeMillis();
			con = JDBCUtils.getConnection();
			con.setAutoCommit(false);
			ps = con.prepareStatement(sql);
			for (int i = 0; i < 100000; i++) {
				ps.setString(1, "person" + i);
				ps.setString(2, "one" + i + "@126.com");
				ps.setInt(3, i % 20);
				ps.addBatch();
				if (i % 300 == 0) {
					ps.executeBatch();
					ps.clearBatch();
				}
			}
			ps.executeBatch();
			ps.clearBatch();
			con.commit();
			long end = System.currentTimeMillis();
			System.out.println(end - start);	//22930
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.release(con, ps);
		}
	}
}
