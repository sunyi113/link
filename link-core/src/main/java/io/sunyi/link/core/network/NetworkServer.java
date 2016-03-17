package io.sunyi.link.core.network;

import io.sunyi.link.core.serialize.SerializeFactory;
import io.sunyi.link.core.server.ServerReceivedHandler;

/**
 * 负责启动服务，接收请求，序列化、反序列化报文。
 *
 * Created by sunyi on 15/9/23.
 */
public interface NetworkServer {

	/**
	 * server port
	 * @return
	 */
	Integer getPort();

	/**
	 * start the server
	 */
	void start() throws Exception;

	/**
	 * close the server
	 */
	void shutdown() throws Exception;

	/**
	 *
	 * @return
	 */
	SerializeFactory getSerializeFactory();


	ServerReceivedHandler getServerReceivedHandler();


}
