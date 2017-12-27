package com.ifox.jdbc.advance;

import java.sql.Connection;

import org.junit.Test;

import com.ifox.jdbc.dao.Dao;
import com.ifox.jdbc.dao.JDBCUtils;

public class IsolutionDemo {
	
	static Dao dao = new Dao();
	static Connection con = null;
	static{
		try {
			con = JDBCUtils.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		/**
		 * 脏读测试
		 */
//		String  sql = "select balance from user where id = 1";
//		try {
//			con.setTransactionIsolation(1);
//			Object obj = dao.selectOne(con, sql);
//			System.out.println(obj);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			JDBCUtils.release(con, null, null);
//		}
//		-------------------------------------------------
//		String sql = "select balance from user where id = 1";
//		try {
//			con.setTransactionIsolation(2);
//			Object obj1 = dao.selectOne(con, sql);
//			System.out.println(obj1);
//			
//			Thread.sleep(1000*10);
//			
//			Object obj2 = dao.selectOne(con, sql);
//			System.out.println(obj2);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			JDBCUtils.release(con, null);
//		}
//		----------------------------------------------------
		String sql = "select balance from user where id = 1";
		try {
			con.setAutoCommit(false);
			con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			Object obj1 = dao.selectOne(con, sql);
			System.out.println(obj1);
			
			Thread.sleep(1000*10);
			
			Object obj2 = dao.selectOne(con, sql);
			System.out.println(obj2);
			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.release(con, null);
		}
		
	}
	
	@Test
	@SuppressWarnings("unused")
	public void testDirtyRead() {
		try {
			con.setAutoCommit(false);
			String sql1 = "update user set balance = balance + 500 where id = 1";
			dao.excute(con, sql1);
			Thread.sleep(1000*10);
			int i = 10/0;
			
			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.release(con, null, null);
		}
		
		
	}
	
	@Test
	public void testNotRepeatReadable() {
		try {
			String sql = "update user set balance = balance - 500 where id = 1";
			dao.excute(con, sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.release(con, null, null);
		}
	}
	
	/**
	 * 幻读测试
	 */
	@Test
	public void testIllusion() {
		try {
			con.setAutoCommit(false);
			con.setTransactionIsolation(8);
			String sql = "update user set balance = balance + 500 where id = 1";
			dao.excute(con, sql);
			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			JDBCUtils.release(con, null, null);
		}
	}

}
