package com.tongbanjie.link.core.network;

/**
 * @author sunyi
 *         Created on 15/9/28
 */
public interface ServerReceivedHandler {

	/**
	 *
	 * @param channel
	 * @param object
	 */
	<T> void received(Channel<T> channel, Object object);

}