package io.sunyi.link.core.network.netty;

import io.sunyi.link.core.serialize.ObjectWriter;
import io.sunyi.link.core.serialize.SerializeFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * @author sunyi
 *         Created on 15/9/23
 */
public class NettyEncode extends MessageToMessageEncoder<Object> {

	private SerializeFactory serializeFactory;
	private ObjectWriter objectWriter;

	public NettyEncode(SerializeFactory serializeFactory) {
		this.serializeFactory = serializeFactory;
		objectWriter = serializeFactory.getObjectWriter();
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
		byte[] bytes = objectWriter.write(msg);
		ByteBuf bb = Unpooled.buffer(bytes.length);
		bb.writeBytes(bytes);
		out.add(bb);
	}
}