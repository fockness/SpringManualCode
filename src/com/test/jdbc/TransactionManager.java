package com.test.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class TransactionManager {
	
	private DataSource dataSource = null;
	private Connection connection = null;
	
	public TransactionManager(DataSource dataSource){
		this.dataSource = dataSource;
	}
	
	private Connection getConnection(DataSource dataSource) throws SQLException{
		return SingleThreadConnectionHolder.getConnection(dataSource);
	}
	
	//开启事务
	public void start() throws SQLException{
		connection = getConnection(dataSource);
		connection.setAutoCommit(false);
	}
	
	//回滚事务
	public void rollBack() throws SQLException{
		connection = getConnection(dataSource);
		connection.rollback();
	}
	
	//关闭事务
	public void close() throws SQLException{
		connection = getConnection(dataSource);
		connection.setAutoCommit(false);
		connection.close();
	}

}
