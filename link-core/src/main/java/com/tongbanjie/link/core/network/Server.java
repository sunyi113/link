package com.tongbanjie.link.core.network;

import com.tongbanjie.link.core.serialize.SerializeFactory;

/**
 * 负责启动服务，接收请求，序列化、反序列化报文。
 *
 * Created by sunyi on 15/9/23.
 */
public interface Server {

	/**
	 * server port
	 * @return
	 */
	int getPort();

	/**
	 * start the server
	 */
	void start() throws Exception;

	/**
	 * shutdown the server
	 */
	void shutdown() throws Exception;

	/**
	 *
	 * @return
	 */
	SerializeFactory getSerializeFactory();


	ServerReceivedHandler getServerReceivedHandler();


}
