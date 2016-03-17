package io.sunyi.link.core.server;

import io.sunyi.link.core.body.RpcRequest;
import io.sunyi.link.core.body.RpcResponse;

/**
 * 服务端处理 RPC 调用的 NetworkServer
 *
 * @param <T>
 * @author sunyi
 */
public class Server<T> {


	private Class<T> serverInterface;


	/**
	 * @return service interface.
	 */
	Class<T> getServerInterface() {
		return serverInterface;
	}


	/**
	 * @param rpcRequest
	 * @return
	 */
	RpcResponse service(RpcRequest rpcRequest) {
		//TODO 反射调用
		return null;
	}

}