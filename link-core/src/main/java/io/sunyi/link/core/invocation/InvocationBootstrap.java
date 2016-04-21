package io.sunyi.link.core.invocation;

import io.sunyi.link.core.context.ApplicationContext;
import io.sunyi.link.core.invocation.proxy.InvocationProxyFactory;
import io.sunyi.link.core.registry.Registry;

/**
 * @author sunyi
 */
public class InvocationBootstrap {

	private static volatile InvocationBootstrap instance;

	/**
	 * 注册中心
	 */
	private Registry registry = ApplicationContext.getRegistry();

	private InvocationBootstrap(){}

	public static InvocationBootstrap getInstance() {
		if (instance != null) {
			return instance;
		}
		synchronized (InvocationBootstrap.class) {
			if (instance != null) {
				return instance;
			}
			instance = new InvocationBootstrap();
			return instance;
		}
	}



	public <T> T getProxy(InvocationConfig<T> invocationConfig) {

		return InvocationProxyFactory.getObject(invocationConfig);
	}

}
