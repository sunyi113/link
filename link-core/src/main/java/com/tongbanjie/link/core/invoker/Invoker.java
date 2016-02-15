package com.tongbanjie.link.core.invoker;

import com.tongbanjie.link.core.body.RpcRequest;
import com.tongbanjie.link.core.body.RpcResponse;
import com.tongbanjie.link.core.exception.RpcException;

/**
 *
 * 客户端发起 RPC 调用的 Invoker
 * @param <T>
 */
public interface Invoker<T>  {

    /**
     * get service interface.
     * 
     * @return service interface.
     */
    Class<T> getInterface();

    /**
     * invoke.
     * 
     * @param rpcRequest
     * @return result rpcResponse
     */
    RpcResponse invoke(RpcRequest rpcRequest);

}