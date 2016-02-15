package com.tongbanjie.link.core.handler;

import com.tongbanjie.link.core.body.RpcRequest;
import com.tongbanjie.link.core.body.RpcResponse;
import com.tongbanjie.link.core.exception.RpcException;

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