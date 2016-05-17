package io.sunyi.link.core.invocation;

import io.sunyi.link.core.commons.LinkApplicationContext;
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
	private Registry registry;


	private InvocationBootstrap() {
	}

	public static InvocationBootstrap getInstance() {
		if (instance != null) {
			return instance;
		}
		synchronized (InvocationBootstrap.class) {
			if (instance != null) {
				return instance;
			}

			InvocationBootstrap invocationBootstrap = new InvocationBootstrap();
			invocationBootstrap.registry = LinkApplicationContext.getRegistry();
			invocationBootstrap.registry.init();

			instance = new InvocationBootstrap();
		}

		return instance;
	}


	public <T> T getProxy(InvocationConfig<T> invocationConfig) {
		InvocationProxyFactory invocationProxyFactory = LinkApplicationContext.getInvocationProxyFactory();
		return invocationProxyFactory.getProxy(invocationConfig);
	}

}
