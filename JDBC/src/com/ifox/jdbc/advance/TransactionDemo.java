package com.ifox.jdbc.advance;

import java.sql.Connection;

import org.junit.Test;

import com.ifox.jdbc.dao.Dao;
import com.ifox.jdbc.dao.JDBCUtils;

public class TransactionDemo {

	Dao dao = new Dao();
	
	@Test
	public void withOutTransaction() {
		Connection con = null;
		try {
			String sql1 = "update user set balance = balance + 500 where id = 1";
			con = JDBCUtils.getConnection();
			dao.excute(con, sql1);
			Thread.sleep(1000*10);
			String sql2 = "update user set balance = balance - 500 where id = 2";
			dao.excute(con, sql2);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.release(con, null);
		}
	}
	
	
	@Test
	public void doTransaction() {
		Connection con = null;
		try {
			String sql1 = "update user set balance = balance + 500 where id = 1";
			con = JDBCUtils.getConnection();
			System.out.println(con.getTransactionIsolation());
			con.setAutoCommit(false);
			dao.excute(con, sql1);
			Thread.sleep(1000*5);
			String sql2 = "update user set balance = balance - 500 where id = 2";
			dao.excute(con, sql2);
			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
