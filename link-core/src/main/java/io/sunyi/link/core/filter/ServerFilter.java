package io.sunyi.link.core.filter;

import io.sunyi.link.core.LinkScalableComponent;
import io.sunyi.link.core.body.RpcRequest;
import io.sunyi.link.core.body.RpcResponse;

/**
 * @author sunyi
 */
public interface ServerFilter extends LinkScalableComponent {

	/**
	 * 在反射调用前执行的
	 *
	 * @param rpcRequest
	 */
	void preInvoke(RpcRequest rpcRequest);

	/**
	 * 等待反射调用结束后执行
	 * @param rpcRequest
	 * @param rpcResponse
	 */
	void afterInvoke(RpcRequest rpcRequest, RpcResponse rpcResponse);


}