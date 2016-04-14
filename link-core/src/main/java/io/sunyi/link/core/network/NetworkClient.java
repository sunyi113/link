package io.sunyi.link.core.network;

import io.sunyi.link.core.body.RpcRequest;
import io.sunyi.link.core.body.RpcResponse;
import io.sunyi.link.core.serialize.SerializeFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * Created by sunyi on 15/9/23.
 */
public interface NetworkClient {


	void connection(InetSocketAddress inetSocketAddress);

	RpcResponse send(RpcRequest rpcRequest, Long sendTimeout, Long timeout);

	/**
	 *
	 * @return
	 */
	SerializeFactory getSerializeFactory();

	boolean isActive();

	void close();

}
