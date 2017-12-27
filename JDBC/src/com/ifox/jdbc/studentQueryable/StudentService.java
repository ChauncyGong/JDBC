package com.ifox.jdbc.studentQueryable;

import java.lang.reflect.Field;
import java.util.Scanner;

import org.junit.Test;

import com.ifox.jdbc.entities.Student;

public class StudentService {
	
	Scanner scanner = new Scanner(System.in);
	
	private StudentDao studentDao = new StudentDaoImpl();
	
	public void addStudent() throws Exception {
		Class<Student> clz = Student.class;
		Field[] fields = clz.getDeclaredFields();
		String value = null;
		Object arg = null;
		Student student = clz.newInstance();
		for (Field field : fields) {
			System.out.print(field.getName() + ": ");
			value = scanner.next();
			/**
			 * 使用反射，通过方法给属性赋值
			 */
//			Method method = clz.getMethod(getMethodName(field.getName()), field.getType());
			if (field.getType() == int.class) {
				arg = Integer.valueOf(value);
			} else {
				arg = value;
			}
//			method.invoke(student, arg);
			/**
			 * 使用反射，直接给字段赋值
			 */
			field.setAccessible(true);
			field.set(student, arg);
		}
		studentDao.addStudent(student);
	}
	
	/**
	 * 通过字段得到getter方法名
	 * @return
	 */
//	private String getMethodName(String field) {
//		char[] chars = field.toCharArray();
//		chars[0] -= 32;
//		return "set" + new String(chars);
//	}

	private Student selectStudent() {
		System.out.println("请选择查询方式：    1、按身份证查询        2、按准考证查询");
		Integer option = Integer.valueOf(scanner.next());
		if (option == 1) {
			System.out.print("请输入身份证号：");
			String identity = scanner.next();
			return studentDao.getStudent(identity, Type.ID_CARD);
		} else if (option == 2) {
			String identity = scanner.next();
			System.out.println("请输入准考证号：");
			return studentDao.getStudent(identity, Type.EXAM_NUM);
		} else {
			throw new IllegalArgumentException();
		}
		
	}
	
	@Test
	public void testStudentOperation() throws Exception {
		System.out.println("请选择操作： 1、添加信息    2、查询信息");
		Integer option = Integer.valueOf(scanner.next());
		if (option == 1) {
			addStudent();
		} else if (option ==2) {
			Student student = selectStudent();
			System.out.println(student);
		} else {
			throw new IllegalArgumentException();
		}
	}

}