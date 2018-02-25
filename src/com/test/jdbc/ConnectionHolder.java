package com.test.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;
/**
 * 一个线程下，一个事务的多个操作拿到的是一个Connection，显然使用ConcurrentHashMap根本无法保证！ 
 * 实现DataSource到数据库管道的路由
 */
public class ConnectionHolder {
	
	private Map<DataSource, Connection> map = new HashMap<DataSource, Connection>();
	
	public Connection getConnectionByDataSource(DataSource source) throws SQLException{
		Connection connection = map.get(source);
		if(connection == null || connection.isClosed()){
			connection = source.getConnection();
			map.put(source, connection);
		}
		return connection;
	}
}
