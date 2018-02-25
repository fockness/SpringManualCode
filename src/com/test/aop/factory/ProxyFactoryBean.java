package com.test.aop.factory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import com.test.ioc.annotation.Component;

@Component
public class ProxyFactoryBean {

	private Object target;

	private InvocationHandler handler;

	public ProxyFactoryBean(Object target, InvocationHandler handler) {
		this.target = target;
		this.handler = handler;
	}

	// 返回本类的一个实例
	public Object getProxyBean() throws IllegalArgumentException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		Object obj = Proxy.newProxyInstance(target.getClass().getClassLoader(),
				target.getClass().getInterfaces(), handler);
		return obj;
	}
}
