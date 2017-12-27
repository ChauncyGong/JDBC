package com.ifox.jdbc.studentQueryable;

import com.ifox.jdbc.entities.Student;

interface StudentDao {
	
	void addStudent(Student stu);

	Student getStudent(String identity, Type type);
}
