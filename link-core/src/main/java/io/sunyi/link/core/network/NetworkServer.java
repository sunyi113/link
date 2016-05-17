package io.sunyi.link.core.network;

import io.sunyi.link.core.commons.LinkScalableComponent;

/**
 * 负责启动服务，接收请求，序列化、反序列化报文。
 * <p/>
 * Created by sunyi on 15/9/23.
 */
public interface NetworkServer extends LinkScalableComponent {

	/**
	 * server port
	 *
	 * @return
	 */
	Integer getPort();

	/**
	 * start the server
	 */
	void start();

	/**
	 * close the server
	 */
	void shutdown();


}
