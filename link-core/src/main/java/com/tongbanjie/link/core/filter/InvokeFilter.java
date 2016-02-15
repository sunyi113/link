package com.tongbanjie.link.core.filter;

import com.tongbanjie.link.core.body.RpcRequest;
import com.tongbanjie.link.core.body.RpcResponse;
import com.tongbanjie.link.core.exception.RpcException;
import com.tongbanjie.link.core.invoker.Invoker;

/**
 * Created by sunyi on 15/9/22.
 */
public interface InvokeFilter {

	RpcResponse invoke(Invoker<?> invoker, RpcRequest rpcRequest) throws RpcException;

}