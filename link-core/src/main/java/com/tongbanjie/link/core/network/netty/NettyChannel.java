package com.tongbanjie.link.core.network.netty;

import com.tongbanjie.link.core.body.RpcRequest;
import com.tongbanjie.link.core.body.RpcResponse;
import com.tongbanjie.link.core.network.Channel;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author sunyi
 *         Created on 15/9/28
 */
public class NettyChannel implements com.tongbanjie.link.core.network.Channel<io.netty.channel.Channel> {

	private final io.netty.channel.Channel originalChannel;
	/**
	 * Message ID
	 */
	private AtomicLong mId = new AtomicLong(0L);

	public NettyChannel(io.netty.channel.Channel originalChannel) {
		this.originalChannel = originalChannel;
	}

	@Override
	public io.netty.channel.Channel getOriginalChannel() {
		return originalChannel;
	}

	@Override
	public void writeResponse(RpcResponse response) {
		originalChannel.writeAndFlush(response);
	}

	@Override
	public void onResponse(RpcResponse response) {

	}

	@Override
	public RpcResponse writeRequestAndGetResponse(RpcRequest request, TimeUnit timeUnit, long timeout) {
		originalChannel.writeAndFlush(request);

		return null;
	}


	@Override
	public boolean isActive() {
		return originalChannel.isActive();
	}

}
