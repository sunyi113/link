package io.sunyi.link.core.invocation.proxy;

import io.sunyi.link.core.body.RpcRequest;
import io.sunyi.link.core.registry.Registry;
import io.sunyi.link.core.server.ServerConfig;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * @author sunyi
 */
public class InvocationProxyFactory {

	private static Registry registry = null;

	public static Object getObject(final Class interfaceClass) {
		// TODO
		InvocationHandlerImpl invocationHandler = new InvocationHandlerImpl(interfaceClass);
		Object proxy = Proxy.newProxyInstance(InvocationProxyFactory.class.getClassLoader(), new Class[]{interfaceClass}, invocationHandler);
		return proxy;
	}


	private static class InvocationHandlerImpl<T> implements  InvocationHandler {


		private final Class<T> interfaceClass;

		public InvocationHandlerImpl(Class<T> interfaceClass) {
			this.interfaceClass = interfaceClass;
		}


		@Override
		public Object invoke(Object o, Method method, Object[] objects) throws Throwable {

			//TODO 封装 PpcRequest 对象
			RpcRequest request = new RpcRequest();



			//TODO 获取服务器列表

			List<ServerConfig> serverList = registry.getServerList();


			//TODO load balance ， 选择一台
			//TODO 发送数据
			//TODO 等待响应
			//TODO 返回结果

			return null;
		}
	}


}
