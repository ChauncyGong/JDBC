package com.ifox.jdbc.dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dao {
	
	public <T> T get(Class<T> clazz, int id) {
		String sql = "SELECT * FROM " + clazz.getSimpleName().toLowerCase() + " WHERE id = ?";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Field[] fields = clazz.getDeclaredFields();
		
		try {
			T entity = clazz.newInstance();
			con = JDBCUtils.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			while (rs.next()) {
				for (int i = 0; i < fields.length; i++) {
					fields[i].setAccessible(true);
					fields[i].set(entity, rs.getObject(i + 1));
				}
				return entity;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			JDBCUtils.release(con, ps, rs);
		}
	}

	/**
	 * 
	 * @param entity 增加的实体对象，对应数据库中同名小写的数据表
	 * 插入实体时,id若存在，则抛出异常，若不存在，数据表采取逐渐自增方式设值
	 */
	@SuppressWarnings({ "unchecked", "resource", "rawtypes" })
	public <T> void save(T entity) {
		Class clz = entity.getClass();
		Field[] fields = clz.getDeclaredFields();
		String sql = SqlUtils.getInsertSql(clz);
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = JDBCUtils.getConnection();
			ps = con.prepareStatement(sql);
			int i = 1;
			for (Field field : fields) {
				field.setAccessible(true);
				if ("id".equals(field.getName())) {
					if (get(clz, (int)field.get(entity)) != null) {
						throw new Exception("对象已存在");
					}
					continue;
				}
				ps.setObject(i++, field.get(entity));
			}
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.release(con, ps, rs);
		}
	}
	
	/**
	 * 删除对象在数据库中对应的记录
	 * @param entity
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> boolean remove(T entity) {
		Class clz = entity.getClass();
		try {
			Field field = clz.getDeclaredField("id");
			field.setAccessible(true);
			int id = (int) field.get(entity);
			return delete(clz, id);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 根据id删除数据库对应的记录
	 * @param clz
	 * @param id
	 */
	public <T> boolean delete(Class<T> clz, int id) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String sql = "DELETE FROM " + clz.getSimpleName().toLowerCase() 
				+ " WHERE id = ?";
		try {
			con = JDBCUtils.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			return ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			JDBCUtils.release(con, ps, rs);
		}
	}
	
	
	/**
	 * 更新实体
	 * @param entity
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "resource" })
	public <T> boolean update(T entity) {
		Class clz = entity.getClass();
		Field[] fields = clz.getDeclaredFields();
		String sql = SqlUtils.getUpdateSql(clz);
		System.out.println(sql);
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			con = JDBCUtils.getConnection();
			ps = con.prepareStatement(sql);
			int i = 1;
			for (Field field : fields) {
				field.setAccessible(true);
				if ("id".equals(field.getName())) {
					if (get(clz, (int)field.get(entity)) == null) {
						throw new Exception("对象不存在");
					}
					ps.setObject(fields.length, field.get(entity));
					continue;
				}
				ps.setObject(i++, field.get(entity));
			}
			return ps.executeUpdate() != 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			JDBCUtils.release(con, ps, rs);
		}
	}
	
	/**
	 * 查询一个对象
	 * @param clazz	查询实体类型
	 * @param sql 查询sql语句
	 * @param args sql语句占位符参数
	 * @return 查询的到的第一个实体对象
	 */
	public <T> T selectOne(Class<T> clazz, String sql, Object... args) {
		List<T> list = getList(clazz, sql, args);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 查询集合对象
	 * @param clazz
	 * @param sql 若实体属性与数据表字段对象，则不需要设值别名，否则需要设置别名，且别名为实体字段名
	 * @param args sql语句占位符参数
	 * @return
	 */
	public <T> List<T> getList(Class<T> clazz, String sql, Object... args) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<T> list = new ArrayList<>();
		
		try {
			T entity = null;
			con = JDBCUtils.getConnection();
			ps = con.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			while (rs.next()) {
				entity = clazz.newInstance();
				for (int i = 0; i < rsmd.getColumnCount(); i++) {
					String fieldName =rsmd.getColumnLabel(i + 1);
					Field field = clazz.getDeclaredField(SqlUtils.transferUnderline(fieldName));
					field.setAccessible(true);
					field.set(entity, rs.getObject(i + 1));
				}
				list.add(entity);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			JDBCUtils.release(con, ps, rs);
		}
	}
	
	/**
	 * 查询多个键值对集合
	 * @param sql
	 * @param args
	 * @return
	 */
	public List<Map<String, Object>> select(String sql,Object... args) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, Object> map = null;
		List<Map<String, Object>> list = new ArrayList<>();
		
		try {
			con = JDBCUtils.getConnection();
			ps = con.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			while (rs.next()) {
				map = new HashMap<>();
				for (int i = 0; i < rsmd.getColumnCount(); i++) {
					String fieldName =rsmd.getColumnLabel(i + 1);
					map.put(fieldName, rs.getObject(i + 1));
				}
				list.add(map);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			JDBCUtils.release(con, ps, rs);
		}
	}
	
	/**
	 * 执行sql语句
	 * @param sql
	 * @param args
	 */
	public void excute(Connection con, String sql, Object... args) {
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtils.release(null, ps);
		}
	}
	
	/**
	 * 执行sql语句
	 * @param sql
	 * @param args
	 */
	public Object selectOne(Connection con, String sql, Object... args) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				ps.setObject(i + 1, args[i]);
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				return rs.getObject(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			JDBCUtils.release(null, ps, rs);
		}
		return null;
	}
}
