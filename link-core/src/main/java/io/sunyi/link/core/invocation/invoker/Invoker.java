package io.sunyi.link.core.invocation.invoker;

import io.sunyi.link.core.body.RpcRequest;
import io.sunyi.link.core.body.RpcResponse;
import io.sunyi.link.core.commons.LinkApplicationContext;
import io.sunyi.link.core.filter.InvocationFilter;
import io.sunyi.link.core.invocation.InvocationConfig;
import io.sunyi.link.core.network.NetworkClient;
import io.sunyi.link.core.server.ServerConfig;

import java.net.InetSocketAddress;
import java.util.List;

public class Invoker<T> {

	private InvocationConfig<T> invocationConfig;
	private NetworkClient networkClient;
	private ServerConfig<T> serverConfig;
	private InetSocketAddress serverAddress;


	public Invoker(InvocationConfig<T> invocationConfig, ServerConfig<T> serverConfig, NetworkClient networkClient) {
		this.invocationConfig = invocationConfig;
		this.serverConfig = serverConfig;
		this.networkClient = networkClient;
		this.serverAddress = new InetSocketAddress(serverConfig.getIp(), serverConfig.getPort());
	}


	public void close() {
		if (networkClient != null) {
			networkClient.close();
		}
	}

	public RpcResponse invoke(RpcRequest rpcRequest) {


		List<InvocationFilter> invocationFilters = LinkApplicationContext.getInvocationFilters();

		for (InvocationFilter filter : invocationFilters) {
			filter.preInvoke(this, rpcRequest);
		}

		RpcResponse rpcResponse = networkClient.send(rpcRequest, invocationConfig.getTimeout());

		for (InvocationFilter filter : invocationFilters) {
			filter.afterInvoke(this, rpcRequest, rpcResponse);
		}

		return rpcResponse;
	}


	public InetSocketAddress getServerAddress() {
		return serverAddress;
	}

	public boolean isActive() {
		return networkClient.isActive();
	}

	public InvocationConfig<T> getInvocationConfig() {
		return invocationConfig;
	}

	public NetworkClient getNetworkClient() {
		return networkClient;
	}

	public ServerConfig<T> getServerConfig() {
		return serverConfig;
	}
}
