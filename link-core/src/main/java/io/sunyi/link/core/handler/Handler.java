package io.sunyi.link.core.handler;

import io.sunyi.link.core.body.RpcRequest;
import io.sunyi.link.core.body.RpcResponse;

/**
 * 服务端处理 RPC 调用的 Handler
 * @param <T>
 * @author sunyi
 */
public interface Handler<T> {

	/**
	 * @return service interface.
	 */
	Class<T> getInterface();

	/**
	 *
	 * @param rpcRequest
	 * @return
	 */
	RpcResponse handle(RpcRequest rpcRequest);

}