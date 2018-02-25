package com.test.aop.aspect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.test.ioc.annotation.Autowired;
import com.test.ioc.annotation.Component;

@Component
public class UserHandler implements InvocationHandler {

	private Object target;// 目标是不固定

	public void setTarget(Object target) {
		this.target = target;
	}
	
	/*
	 * return 返回是原来目标方法所返回的内容 method 就是要执行的方法
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		before();
		// 具体的业务逻辑代码
		Object returnValue = method.invoke(target, args);
		after();
		return returnValue;
	}

	private void before() {
		// 前置任务
		System.out.println("[代理执行前置任务]整理着装");
		System.out.println("[代理执行前置任务]带上钥匙");
		System.out.println("");
		System.out.println("[核心业务开始]*****************");
	}

	private void after() {
		// 后置任务
		System.out.println("[核心业务结束]*****************");
		System.out.println("");
		System.out.println("[代理执行后置任务]开门");
		System.out.println("[代理执行后置任务]换鞋");
	}

}
