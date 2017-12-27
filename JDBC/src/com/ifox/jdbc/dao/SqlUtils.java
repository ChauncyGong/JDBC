package com.ifox.jdbc.dao;

import java.lang.reflect.Field;

public class SqlUtils {

	public SqlUtils() {}
	
	/**
	 * 属性驼峰命名转化为下划线风格
	 * @param str
	 * @return
	 */
	public static String transferCamelCase(String str) {
		StringBuilder sb = new StringBuilder();
		char[] charArray = str.toCharArray();
		for (char c : charArray) {
			if (c > 65 && c < 90) {
				sb.append("_" + (char)(c + 32));
				continue;
			}
			sb.append(c);
		}
		return sb.toString();
	}
	
	/**
	 * 下划线风格转化为驼峰命名
	 * @param str
	 * @return
	 */
	public static String transferUnderline(String str) {
		StringBuilder sb = new StringBuilder();
		char[] charArray = str.toCharArray();
		boolean flag = false;
		for (char c : charArray) {
			if (c == '_') {
				flag = true;
				continue;
			}
			if(flag) {
				sb.append((char)(c - 32));
				flag = false;
				continue;
			}
			sb.append(c);
		}
		return sb.toString();
	}
	
	/**
	 * 根据实体对象类型获取插入sql语句
	 * @param clazz	实体对象的类型
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String getInsertSql(Class clazz) {
		StringBuilder sb = new StringBuilder("INSERT INTO ");
		sb.append(clazz.getSimpleName().toLowerCase());
		sb.append("(");
		Field[] fields = clazz.getDeclaredFields();
		for (Field field: fields) {
			if ("id".equals(field.getName())) {
				continue;
			}
			sb.append(transferCamelCase(field.getName()) + ", ");
		}
		sb.delete(sb.length() - 2, sb.length());
		sb.append(") VALUES ");
		int length = fields.length - 1;
		sb.append("(");
		while (length > 0) {
			sb.append("?, ");
			length--;
		}
		sb.delete(sb.length() - 2, sb.length());
		sb.append(")");
		return sb.toString();
	}
	
	
	/**
	 * 获取更新sql语句
	 * @param clz 需更新的实体类名
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String getUpdateSql(Class clz) {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE " + clz.getSimpleName().toLowerCase() + " SET ");
		Field[] fields = clz.getDeclaredFields();
		for (Field field : fields) {
			if ("id".equals(field.getName())) {
				continue;
			}
			sb.append(transferCamelCase(field.getName()) + " = ? ,");
		}
		sb.delete(sb.length() - 2, sb.length());
		sb.append(" WHERE id = ?");
		return sb.toString();
	}
	
}
