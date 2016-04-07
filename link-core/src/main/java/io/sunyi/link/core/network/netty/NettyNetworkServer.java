package io.sunyi.link.core.network.netty;

import io.sunyi.link.core.body.RpcRequest;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.sunyi.link.core.body.RpcResponse;
import io.sunyi.link.core.exception.LinkRuntimeException;
import io.sunyi.link.core.network.NetworkServer;
import io.sunyi.link.core.serialize.hessian.HessianSerializeFactory;
import io.sunyi.link.core.server.ServerReceivedHandler;
import io.sunyi.link.core.serialize.SerializeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sunyi
 *         Created on 15/9/23
 */
public class NettyNetworkServer implements NetworkServer {

	private Logger logger = LoggerFactory.getLogger(NettyNetworkServer.class);

	private Integer port;

	protected static final ConcurrentHashMap<io.netty.channel.Channel, io.sunyi.link.core.network.Channel<Channel>> channels =
			new ConcurrentHashMap<Channel, io.sunyi.link.core.network.Channel<Channel>>();

	protected EventLoopGroup bossGroup = new NioEventLoopGroup();
	protected EventLoopGroup workerGroup = new NioEventLoopGroup();
	protected ChannelFuture f = null;
	protected Channel channel = null;

	protected int SO_BACKLOG = 1024;

	private ServerReceivedHandler serverReceivedHandler = new ServerReceivedHandler();

	public NettyNetworkServer(Integer port) {
		this.port = port;
	}

	@Override
	public Integer getPort() {
		return this.port;
	}



	@Override
	public void start() throws Exception {

		if (getSerializeFactory() == null) {
			throw new LinkRuntimeException("SerializeFactory must not be null");
		}

		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 1024)
				.childHandler(new ChannelInitializer() {

					@Override
					protected void initChannel(Channel ch) throws Exception {
						ch.pipeline().addLast("LengthFieldPrepender", new LengthFieldPrepender(4));
						ch.pipeline().addLast("LengthFieldBasedFrameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));

						ch.pipeline().addLast("encode", new NettyEncode(getSerializeFactory()));
						ch.pipeline().addLast("decode", new NettyDecode(getSerializeFactory()));

						ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {

							@Override
							public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
								if (!(msg instanceof RpcRequest)) {
									// TODO delete sout
									System.out.println("msg instanceof RpcRequest == false");
									return;
								}

								RpcRequest request = (RpcRequest) msg;

								ServerReceivedHandler handler = getServerReceivedHandler();

								RpcResponse response = handler.received(request);

								ctx.writeAndFlush(response);

							}
						});
					}
				});

		f = b.bind(getPort()).sync();
		channel = f.channel();


		logger.info(NettyNetworkServer.class.getSimpleName() + " started.");

	}

	@Override
	public void shutdown() throws Exception {
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
		channel.close().sync();

		logger.info(NettyNetworkServer.class.getSimpleName() + " shutdown.");
	}

	@Override
	public SerializeFactory getSerializeFactory() {
		return HessianSerializeFactory.getInstance();
	}

	@Override
	public ServerReceivedHandler getServerReceivedHandler() {
		return null;
	}


}


