package com.test.ioc.service;

import com.test.ioc.annotation.Service;

@Service
public class UserService {
	
	public void say(){
		System.out.println("hello");
	}
}
