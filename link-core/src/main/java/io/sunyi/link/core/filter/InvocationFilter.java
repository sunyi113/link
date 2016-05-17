package io.sunyi.link.core.filter;

import io.sunyi.link.core.commons.LinkScalableComponent;
import io.sunyi.link.core.body.RpcRequest;
import io.sunyi.link.core.body.RpcResponse;
import io.sunyi.link.core.invocation.invoker.Invoker;

/**
 * 调用方的Filter
 *
 * @author sunyi
 */
public interface InvocationFilter extends LinkScalableComponent {

	/**
	 * 在发起远程调用前执行
	 */
	void preInvoke(Invoker invoker, RpcRequest rpcRequest);

	/**
	 * 等待远程调用结束后执行
	 */
	void afterInvoke(Invoker invoker, RpcRequest rpcRequest, RpcResponse rpcResponse);

}