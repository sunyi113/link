package io.sunyi.link.core.invocation.proxy.jdk;

import io.sunyi.link.core.invocation.InvocationConfig;
import io.sunyi.link.core.invocation.proxy.InvocationProxy;
import io.sunyi.link.core.invocation.proxy.InvocationProxyFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author sunyi
 */
public class JdkInvocationProxyFactory implements InvocationProxyFactory {


	public <T> T getProxy(InvocationConfig<T> invocationConfig) {
		InvocationHandlerImpl invocationHandler = new InvocationHandlerImpl(invocationConfig);
		Class<T> interfaceClass = invocationConfig.getInterfaceClass();
		return (T) Proxy.newProxyInstance(JdkInvocationProxyFactory.class.getClassLoader(), new Class[]{interfaceClass}, invocationHandler);
	}


	private static class InvocationHandlerImpl<T> implements InvocationHandler {

		private final InvocationProxy<T> proxy;


		public InvocationHandlerImpl(InvocationConfig<T> invocationConfig) {
			this.proxy = new InvocationProxy<T>(invocationConfig);
		}


		@Override
		public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
			return proxy.invoke(o, method, objects);
		}
	}

}
