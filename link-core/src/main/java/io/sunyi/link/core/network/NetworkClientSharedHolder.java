package io.sunyi.link.core.network;

import io.sunyi.link.core.context.LinkApplicationContext;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sunyi
 */
public class NetworkClientSharedHolder {

	/**
	 * 保存服务器地址与目标的Client
	 */
	private static volatile ConcurrentHashMap<InetSocketAddress, NetworkClientReferenceCount> clients = new ConcurrentHashMap<InetSocketAddress, NetworkClientReferenceCount>();


	/**
	 * 为一个新的Invocation 获取目标服务器地址共享的网络连接客户端<br/>
	 * 使用引用计数器方式，来决定是否真实的关闭连接。
	 *
	 * @param inetSocketAddress
	 * @return
	 */
	public static NetworkClient getSharedNetworkClient(InetSocketAddress inetSocketAddress) {

		NetworkClientReferenceCount rcNetworkClient = clients.get(inetSocketAddress);

		if (rcNetworkClient != null) {

			if (rcNetworkClient.isActive()) {
				rcNetworkClient.increment();
				return rcNetworkClient;
			} else {
				NetworkClient networkClient = rcNetworkClient.getNetworkClient();
				networkClient.connection(inetSocketAddress);
				return networkClient;
			}
		}

		NetworkClientFactory networkClientFactory = LinkApplicationContext.getNetworkClientFactory();
		NetworkClient networkClient = networkClientFactory.getNetworkClient();
		networkClient.connection(inetSocketAddress);

		rcNetworkClient = new NetworkClientReferenceCount(networkClient);
		rcNetworkClient.increment();

		clients.put(inetSocketAddress, rcNetworkClient);

		return networkClient;

	}

	public static NetworkClient getNetworkClient(InetSocketAddress inetSocketAddress) {
		return clients.get(inetSocketAddress);
	}


}
