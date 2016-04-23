package io.sunyi.link.core.filter;

import io.sunyi.link.core.body.RpcRequest;
import io.sunyi.link.core.body.RpcResponse;
import io.sunyi.link.core.server.ServerConfig;

/**
 *
 * 调用方的Filter
 *
 * @author sunyi
 */
public interface InvocationFilter {

	/**
	 * 在发起调用前执行的
	 *
	 * @param rpcRequest
	 */
	void preInvoke(RpcRequest rpcRequest, ServerConfig serverConfig);


	void afterInvoke(RpcRequest rpcRequest, RpcResponse rpcResponse);

}
