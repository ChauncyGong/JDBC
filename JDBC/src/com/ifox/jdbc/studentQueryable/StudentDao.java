package com.ifox.jdbc.studentQueryable;

interface StudentDao {
	
	void addStudent(Student stu);

	Student getStudent(String identity, Type type);
}
