package io.sunyi.link.core.network;

import io.sunyi.link.core.body.RpcRequest;
import io.sunyi.link.core.body.RpcResponse;
import io.sunyi.link.core.serialize.SerializeFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author sunyi
 */
public class ReferenceCountNetworkClient implements NetworkClient {

	private final AtomicLong count = new AtomicLong(0);

	private final NetworkClient networkClient;

	public ReferenceCountNetworkClient(NetworkClient networkClient) {
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
	public RpcResponse send(RpcRequest rpcRequest, Long sendTimeout, Long timeout) {
		return networkClient.send(rpcRequest, sendTimeout, timeout);
	}

	@Override
	public SerializeFactory getSerializeFactory() {
		return networkClient.getSerializeFactory();
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
