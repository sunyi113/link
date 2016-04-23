package io.sunyi.link.core.context;

import io.sunyi.link.core.invocation.loadbalance.LoadBalance;
import io.sunyi.link.core.invocation.proxy.InvocationProxyFactory;
import io.sunyi.link.core.network.NetworkClient;
import io.sunyi.link.core.network.NetworkServer;
import io.sunyi.link.core.network.netty.NettyNetworkClient;
import io.sunyi.link.core.registry.Registry;
import io.sunyi.link.core.serialize.SerializeFactory;
import io.sunyi.link.core.server.ServerReceivedHandler;

/**
 * 为了简单的实现功能，
 * 模拟了Spring 的 Application ， 类似 dubbo 的 ExtensionLoader
 *
 * @author sunyi
 */
public class ApplicationContext {

	/**
	 * 注册中心
	 */
	private static Registry registry = null;

	/**
	 * 网络服务的服务端
	 */
	private static NetworkServer networkServer = null;

	/**
	 * 网络通讯的序列化工厂
	 */
	private static SerializeFactory serializeFactory = null;

	private static ServerReceivedHandler serverReceivedHandler = null;

	private static InvocationProxyFactory invocationProxyFactory = null;

	private static LoadBalance loadBalance = null;

	public static Registry getRegistry() {
		return registry;
	}

	public static NetworkServer getNetworkServer() {
		return networkServer;
	}

	public static void setRegistry(Registry registry) {
		ApplicationContext.registry = registry;
	}

	public static void setNetworkServer(NetworkServer networkServer) {
		ApplicationContext.networkServer = networkServer;
	}

	public static NetworkClient getNewNetworkClient() {
		return new NettyNetworkClient();
	}

	public static SerializeFactory getSerializeFactory() {
		return serializeFactory;
	}

	public static void setSerializeFactory(SerializeFactory serializeFactory) {
		ApplicationContext.serializeFactory = serializeFactory;
	}

	public static ServerReceivedHandler getServerReceivedHandler() {
		return serverReceivedHandler;
	}

	public static void setServerReceivedHandler(ServerReceivedHandler serverReceivedHandler) {
		ApplicationContext.serverReceivedHandler = serverReceivedHandler;
	}

	public static InvocationProxyFactory getInvocationProxyFactory() {
		return invocationProxyFactory;
	}

	public static void setInvocationProxyFactory(InvocationProxyFactory invocationProxyFactory) {
		ApplicationContext.invocationProxyFactory = invocationProxyFactory;
	}

	public static LoadBalance getLoadBalance() {
		return loadBalance;
	}

	public static void setLoadBalance(LoadBalance loadBalance) {
		ApplicationContext.loadBalance = loadBalance;
	}
}
