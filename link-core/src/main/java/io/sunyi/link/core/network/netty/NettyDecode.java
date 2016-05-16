package io.sunyi.link.core.network.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.sunyi.link.core.serialize.Serialize;
import io.sunyi.link.core.serialize.SerializeFactory;

import java.util.List;

/**
 * @author sunyi
 *         Created on 15/9/28
 */
public class NettyDecode extends MessageToMessageDecoder<ByteBuf> {


	private SerializeFactory serializeFactory;
	private Serialize serialize;

	public NettyDecode(SerializeFactory serializeFactory) {
		this.serializeFactory = serializeFactory;
		this.serialize = serializeFactory.getSerialize();
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List out) throws Exception {

		byte[] bytes = new byte[msg.readableBytes()];
		msg.readBytes(bytes);

		Object object = serialize.read(bytes);
		out.add(object);
	}

}
