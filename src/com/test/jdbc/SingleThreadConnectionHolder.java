package com.test.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * 
 * 本来线程不安全的，通过ThreadLocal这么封装一下，立刻就变成了线程的局部变量，
 * 不仅仅安全了，还保证了同一个线程下面的操作拿到的Connection是同一个对象！这种思想，确实非常巧妙，这也是无锁编程思想的一种方式！
 */
public class SingleThreadConnectionHolder {
	
	private static ThreadLocal<ConnectionHolder> threadLocal = new ThreadLocal<ConnectionHolder>();
	
	private static	ConnectionHolder getConnectionHolder(){
		ConnectionHolder connectionHolder = threadLocal.get();
		if(connectionHolder == null){
			connectionHolder = new ConnectionHolder();
			threadLocal.set(connectionHolder);
		}
		return connectionHolder;
	}
	
	public static Connection getConnection(DataSource source) throws SQLException{
		return getConnectionHolder().getConnectionByDataSource(source);
	}

}
