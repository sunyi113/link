package io.sunyi.link.core.filter;

import io.sunyi.link.core.body.RpcRequest;
import io.sunyi.link.core.body.RpcResponse;
import io.sunyi.link.core.exception.RpcException;
import io.sunyi.link.core.invoker.Invoker;

/**
 * Created by sunyi on 15/9/22.
 */
public interface InvokeFilter {

	RpcResponse invoke(Invoker<?> invoker, RpcRequest rpcRequest) throws RpcException;

}