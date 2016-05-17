package io.sunyi.link.core.network;

import io.sunyi.link.core.body.RpcRequest;
import io.sunyi.link.core.body.RpcResponse;
import io.sunyi.link.core.commons.LinkApplicationContext;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 一个服务端可能发布多个远程服务，但网络连接（Network）只需要一个，这个类帮助持有连接并复用。
 *
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
		if (rcNetworkClient == null) {
			synchronized (NetworkClientSharedHolder.class) {
				// 防止重复创建连接，增加同步锁。
				rcNetworkClient = clients.get(inetSocketAddress);
				if (rcNetworkClient == null) {
					NetworkClientFactory networkClientFactory = LinkApplicationContext.getNetworkClientFactory();
					NetworkClient networkClient = networkClientFactory.getNetworkClient();
					networkClient.connection(inetSocketAddress);

					rcNetworkClient = new NetworkClientReferenceCount(networkClient);
					clients.put(inetSocketAddress, rcNetworkClient);
				}
			}
		}

		// 引用计数加一
		rcNetworkClient.increment();

		if (rcNetworkClient != null) {
			if (rcNetworkClient.isActive()) {
			} else {
				NetworkClient networkClient = rcNetworkClient.getNetworkClient();
				networkClient.connection(inetSocketAddress);
			}
		}

		return rcNetworkClient;

	}

	public static NetworkClient getNetworkClient(InetSocketAddress inetSocketAddress) {
		return clients.get(inetSocketAddress);
	}

	private static class NetworkClientReferenceCount implements NetworkClient {

		private final AtomicLong count = new AtomicLong(0);

		private final NetworkClient networkClient;

		public NetworkClientReferenceCount(NetworkClient networkClient) {
			this.networkClient = networkClient;
		}

		public void increment() {
			count.incrementAndGet();
		}

		public NetworkClient getNetworkClient() {
			return networkClient;
		}

		@Override
		public void connection(InetSocketAddress inetSocketAddress) {
			networkClient.connection(inetSocketAddress);
		}

		@Override
		public RpcResponse send(RpcRequest rpcRequest, Long timeout) {
			return networkClient.send(rpcRequest, timeout);
		}

		@Override
		public boolean isActive() {
			return networkClient.isActive();
		}

		@Override
		public void close() {
			if (count.decrementAndGet() <= 0) {
				networkClient.close();
			}
		}
	}


}
