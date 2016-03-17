package io.sunyi.link.core.invocation;

import io.sunyi.link.core.body.RpcRequest;
import io.sunyi.link.core.body.RpcResponse;

/**
 *
 * 客户端发起 RPC 调用的 Invocation
 * @param <T>
 */
public interface Invocation<T>  {

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