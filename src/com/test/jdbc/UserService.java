package com.test.jdbc;

import java.sql.SQLException;

import javax.sql.DataSource;

public class UserService {
	
	private UserAccountDao userAccountDao;
	private UserOrderDao userOrderDao;
	private TransactionManager transactionManager;
	
	public UserService(DataSource dataSource){
		userAccountDao = new UserAccountDao(dataSource);
		userOrderDao = new UserOrderDao(dataSource);
		transactionManager = new TransactionManager(dataSource);
	}
	
	public void action() throws SQLException{
		try {
			transactionManager.start();
			userAccountDao.buy();
			userOrderDao.order();
			transactionManager.close();
		} catch (SQLException e) {
			e.printStackTrace();
			transactionManager.rollBack();
		}
		
	}

}
