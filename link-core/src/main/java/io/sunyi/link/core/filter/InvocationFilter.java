package io.sunyi.link.core.filter;

import io.sunyi.link.core.LinkScalableComponent;
import io.sunyi.link.core.body.RpcRequest;
import io.sunyi.link.core.body.RpcResponse;
import io.sunyi.link.core.invocation.invoker.Invoker;
import io.sunyi.link.core.server.ServerConfig;

/**
 * 调用方的Filter
 *
 * @author sunyi
 */
public interface InvocationFilter extends LinkScalableComponent {

	/**
	 * 在发起远程调用前执行的，如果返回的 RpcResponse 不为 null ，则替换真实返回结果
	 *
	 * @param rpcRequest
	 */
	RpcResponse preInvoke(RpcRequest rpcRequest, Invoker invoker);

	/**
	 * 等待远程调用结束后执行，如果返回的 RpcResponse 不为 null ，则替换真实返回结果
	 *
	 * @param rpcRequest
	 * @param rpcResponse
	 * @param invoker
	 */
	RpcResponse afterInvoke(RpcRequest rpcRequest, RpcResponse rpcResponse, Invoker invoker);

}
