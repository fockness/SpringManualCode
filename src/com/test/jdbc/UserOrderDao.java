package com.test.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class UserOrderDao {
	
	private DataSource dataSource;
	
	public UserOrderDao(DataSource dataSource){
		this.dataSource = dataSource;
	}
	
	public void order() throws SQLException{
		Connection connection = SingleThreadConnectionHolder.getConnection(dataSource);
		System.out.println("当前用户订单线程:" + Thread.currentThread().getName() + ",使用管道:" + connection.hashCode());
	}

}
