package com.ifox.jdbc.advance;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.Test;

import com.ifox.jdbc.dao.JDBCUtils;

public class BlobDemo {

	@Test
	public void addBlob() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "insert into user(name,picture) values(?,?)";
		
		try {
			con = JDBCUtils.getConnection();
			ps = con.prepareStatement(sql);
			ps.setString(1, "name");
			InputStream in = new FileInputStream("head.jpg");
			ps.setBlob(2, in);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.release(con, ps, rs);
		}
	}
	
	@Test
	public void getBlob() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "select * from user where id = 2";
		try {
			con = JDBCUtils.getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				String name = rs.getString(2);
				System.out.println(name);
				InputStream inStream = rs.getBinaryStream(3);
				byte[] buffer = new byte[1024];
				OutputStream outputStream = new FileOutputStream("export.jpg");
				int len = 0;
				while ((len = inStream.read(buffer, 0, buffer.length)) > 0) {
					outputStream.write(buffer, 0, len);
				}
				inStream.close();
				outputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.release(con, ps, rs);
		}
	}
	
}
