package com.ifox.jdbc.studentQueryable;

import java.sql.ResultSet;
import java.sql.Statement;

import com.mysql.jdbc.Connection;

public class StudentDaoImpl implements StudentDao {

	@Override
	public void addStudent(Student stu) {
		Connection connection = null;
		Statement statement = null;
		
		String sql = "INSERT INTO student VALUES("
				+ stu.getId() + ",'"
				+ stu.getFlowId() +"','"
				+ stu.getName() +"','"
				+ stu.getIdCard() + "','"
				+ stu.getExamNum() + "','"
				+ stu.getSubject() + "',"
				+ stu.getGrade() + ")";
		try {
			connection = JDBCTools.getConnection();
			statement = (Statement) connection.createStatement();
			statement.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCTools.release(statement, connection);
		}
		
	}

	@Override
	public Student getStudent(String identity, Type type) {
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		
		String sql = "SELECT * FROM student WHERE ";
		switch (type) {
		case ID_CARD:
			sql += "id_card = " + identity;
			break;
		case EXAM_NUM:
			sql += "exam_num = " + identity;
			break;
		}
		
		try {
			connection = JDBCTools.getConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(sql);
			
			while (rs.next()) {
				return new Student(rs.getInt(1),
						rs.getString(2),
						rs.getString(3),
						rs.getString(4),
						rs.getString(5),
						rs.getString(6),
						rs.getInt(7));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCTools.release(rs, statement, connection);
		}
		return null;
	}
	
}
