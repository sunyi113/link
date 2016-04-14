package io.sunyi.link.core.invocation;

import io.sunyi.link.core.registry.Registry;

/**
 * @author sunyi
 */
public class InvocationBootstrap {

	private static volatile InvocationBootstrap instance;

	/**
	 * 注册中心
	 */
	private Registry registry = null;

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


	public void setRegistry(Registry registry) {
		this.registry = registry;
	}


	public <T> T getProxy(InvocationConfig<T> invocationConfig) {

		return null; // TODO
	}

}
