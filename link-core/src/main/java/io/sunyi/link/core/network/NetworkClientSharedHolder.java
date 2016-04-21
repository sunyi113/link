package io.sunyi.link.core.network;

import io.sunyi.link.core.context.ApplicationContext;
import io.sunyi.link.core.network.netty.NettyNetworkClient;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sunyi
 */
public class NetworkClientSharedHolder {

	/**
	 * 保存服务器地址与目标的Client
	 */
	private static volatile ConcurrentHashMap<InetSocketAddress, ReferenceCountNetworkClient> clients = new ConcurrentHashMap<InetSocketAddress, ReferenceCountNetworkClient>();


	/**
	 * 为一个新的Invocation 获取目标服务器地址共享的网络连接客户端<br/>
	 * 使用引用计数器方式，来决定是否真实的关闭连接。
	 *
	 * @param inetSocketAddress
	 * @return
	 */
	public static NetworkClient getSharedNetworkClient(InetSocketAddress inetSocketAddress) {

		ReferenceCountNetworkClient rcNetworkClient = clients.get(inetSocketAddress);

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


		NetworkClient networkClient = ApplicationContext.getNewNetworkClient();
		networkClient.connection(inetSocketAddress);

		rcNetworkClient = new ReferenceCountNetworkClient(networkClient);
		rcNetworkClient.increment();

		clients.put(inetSocketAddress, rcNetworkClient);

		return networkClient;

	}

	public static NetworkClient getNetworkClient(InetSocketAddress inetSocketAddress) {
		return clients.get(inetSocketAddress);
	}


}
