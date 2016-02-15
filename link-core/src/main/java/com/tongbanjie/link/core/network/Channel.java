package com.tongbanjie.link.core.network;

import com.tongbanjie.link.core.body.RpcRequest;
import com.tongbanjie.link.core.body.RpcResponse;

import java.util.concurrent.TimeUnit;

/**
 * 网络渠道，类似与Netty的Channel，因为要兼容不同实现，所以抽象出来一层。
 *
 * @author sunyi
 *         Created on 15/9/25
 */
public interface Channel<T> {

	/**
	 * 获得对应网络通讯对应的原声Channel
	 *
	 * @return
	 */
	T getOriginalChannel();

	/**
	 * 响应一个请求
	 *
	 * @param response
	 */
	void writeResponse(RpcResponse response);

	/**
	 * 如果使用异步通讯框架，可能需要通知请求已经响应了，需要触发此方法。例如 Netty 等，如不需要可忽略。
	 *
	 * @param response
	 */
	void onResponse(RpcResponse response);

	/**
	 * 写入一个对象，并等待返回结果，一般用于客户端发起请求。
	 *
	 * @param request
	 * @return
	 */
	RpcResponse writeRequestAndGetResponse(RpcRequest request, TimeUnit timeUnit, long timeout);

	/**
	 * 是否为一个有效的链接。
	 *
	 * @return
	 */
	boolean isActive();


}
