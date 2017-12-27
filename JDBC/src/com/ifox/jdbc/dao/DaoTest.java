package com.ifox.jdbc.dao;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.ifox.jdbc.entities.Student;

public class DaoTest {
	
	Dao dao = new Dao();

	@Test
	public void testGet() {
		Student student = dao.get(Student.class, 1);
		System.out.println(student);
	}

	@Test
	public void testSave() {
		Student student = new Student(6, "10", "10", "10", "10", "pe", 10);
		dao.save(student);
	}

	@Test
	public void testRemove() {
		Student student = new Student(5, "10", "10", "10", "10", "pe", 10);
		boolean remove = dao.remove(student);
		System.out.println(remove);
	}

	@Test
	public void testDelete() {
		boolean delete = dao.delete(Student.class, 126);
		System.out.println(delete);
	}

	@Test
	public void testUpdate() {
		Student student = new Student(5, "20", "20", "20", "20", "music", 20);
		boolean update = dao.update(student);
		System.out.println(update);
	}

	@Test
	public void testSelectOne() {
		Student selectOne = dao.selectOne(Student.class, "select * from student where grade = ?", 100);
		System.out.println(selectOne);
	}

	@Test
	public void testGetList() {
		List<Student> students= dao.getList(Student.class, "select * from student where grade = ?", 100);
		System.out.println(students);
	}

	@Test
	public void testSelect() {
		List<Map<String, Object>> select = dao.select("select name, grade from student where grade > ?", 100);
		for (Map<String, Object> map : select) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				System.out.println(entry.getKey() + " : " + entry.getValue());
			}
		}
	}

}
