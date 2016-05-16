package io.sunyi.link.core.network.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.sunyi.link.core.serialize.Serialize;
import io.sunyi.link.core.serialize.SerializeFactory;

import java.util.List;

/**
 * @author sunyi
 *         Created on 15/9/23
 */
public class NettyEncode extends MessageToMessageEncoder<Object> {

	private SerializeFactory serializeFactory;
	private Serialize serialize;

	public NettyEncode(SerializeFactory serializeFactory) {
		this.serializeFactory = serializeFactory;
		this.serialize = serializeFactory.getSerialize();
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
		byte[] bytes = serialize.write(msg);
		ByteBuf bb = Unpooled.buffer(bytes.length);
		bb.writeBytes(bytes);
		out.add(bb);
	}
}