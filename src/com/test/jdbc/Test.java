package com.test.jdbc;

import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;


public class Test {

	public static final String jdbcDriver = "com.mysql.jdbc.Driver";
	public static final String jdbcURL = "jdbc:mysql://localhost:3306/mybatis?useSSL=true";
	public static final String jdbcUsername = "root";
	public static final String jdbcPassword = "871255";
	
	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(jdbcDriver);
		dataSource.setUrl(jdbcURL);
		dataSource.setUsername(jdbcUsername);
		dataSource.setPassword(jdbcPassword);
		
		final UserService userService = new UserService(dataSource);
		
		for(int i=0; i<10; i++){
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						userService.action();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
