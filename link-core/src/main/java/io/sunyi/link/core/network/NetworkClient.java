package io.sunyi.link.core.network;

import io.sunyi.link.core.body.RpcRequest;
import io.sunyi.link.core.body.RpcResponse;
import io.sunyi.link.core.commons.LinkScalableComponent;

import java.net.InetSocketAddress;

/**
 * Created by sunyi on 15/9/23.
 */
public interface NetworkClient extends LinkScalableComponent {


	void connection(InetSocketAddress inetSocketAddress);

	RpcResponse send(RpcRequest rpcRequest, Long timeout);

	boolean isActive();

	void close();

}
