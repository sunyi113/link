package io.sunyi.link.core.context;

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

	private static NetworkServer networkServer = null;

	private static SerializeFactory serializeFactory = null;

	private static ServerReceivedHandler serverReceivedHandler = null;

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
}
