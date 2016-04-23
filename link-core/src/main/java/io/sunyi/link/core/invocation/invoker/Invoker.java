package io.sunyi.link.core.invocation.invoker;

import io.sunyi.link.core.body.RpcRequest;
import io.sunyi.link.core.body.RpcResponse;
import io.sunyi.link.core.invocation.InvocationConfig;
import io.sunyi.link.core.network.NetworkClient;
import io.sunyi.link.core.server.ServerConfig;

import java.net.InetSocketAddress;

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

		RpcResponse rpcResponse = networkClient.send(rpcRequest, invocationConfig.getTimeout());

		return rpcResponse;
	}


	public InetSocketAddress getServerAddress() {
		return serverAddress;
	}

	public boolean isActive() {
		return networkClient.isActive();
	}


}
