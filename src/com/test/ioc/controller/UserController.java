package com.test.ioc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.test.ioc.annotation.Autowired;
import com.test.ioc.annotation.Controller;
import com.test.ioc.annotation.RequestMapping;
import com.test.ioc.service.UserService;

@Controller("userController")
public class UserController {

	@Autowired("userService") 
	private UserService userService;
	
	@RequestMapping("/say")
	public void sayHello(HttpServletRequest request, HttpServletResponse response, String param){
		userService.say();
	}
}
