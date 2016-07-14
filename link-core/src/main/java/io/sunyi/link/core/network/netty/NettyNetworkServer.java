package io.sunyi.link.core.network.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.sunyi.link.core.body.RpcRequest;
import io.sunyi.link.core.body.RpcResponse;
import io.sunyi.link.core.commons.LinkApplicationContext;
import io.sunyi.link.core.exception.LinkException;
import io.sunyi.link.core.network.NetworkServer;
import io.sunyi.link.core.serialize.SerializeFactory;
import io.sunyi.link.core.server.handler.ServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @author sunyi
 */
public class NettyNetworkServer implements NetworkServer {

	private Logger logger = LoggerFactory.getLogger(NettyNetworkServer.class);

	private volatile Integer port;


	private EventLoopGroup bossGroup = new NioEventLoopGroup();
	private EventLoopGroup workerGroup = new NioEventLoopGroup();
	private ChannelFuture f = null;
	private Channel channel = null;

	private SerializeFactory serializeFactory;
	private ServerHandler serverHandler;

	public NettyNetworkServer(Integer port) {
		this.port = port;
		this.serializeFactory = LinkApplicationContext.getSerializeFactory();
		this.serverHandler = ServerHandler.getInstance();
	}

	@Override
	public Integer getPort() {
		return this.port;
	}


	@Override
	public void start() {

		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 1024)
					.childHandler(new ChannelInitializer() {

						@Override
						protected void initChannel(Channel ch) throws Exception {
							ch.pipeline().addLast("LengthFieldPrepender", new LengthFieldPrepender(4));
							ch.pipeline().addLast("LengthFieldBasedFrameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));

							ch.pipeline().addLast("encode", new NettyEncode(serializeFactory));
							ch.pipeline().addLast("decode", new NettyDecode(serializeFactory));

							ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {

								@Override
								public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

									if (!(msg instanceof RpcRequest)) {
										logger.warn("NetworkServer read the message, but not instance of RpcRequest.");
										return;
									}

									RpcRequest request = (RpcRequest) msg;

									RpcResponse response = serverHandler.received(request);

									ctx.writeAndFlush(response);

								}
							});
						}
					});

			f = b.bind(getPort() == null ? 0 : getPort()).sync();
			channel = f.channel();

			port = ((InetSocketAddress) channel.localAddress()).getPort();

			logger.info(NettyNetworkServer.class.getSimpleName() + " started, port: " + port + ".");
		} catch (InterruptedException e) {
			throw new LinkException("NettyNetworkServer start fail.", e);
		}

	}

	@Override
	public void shutdown() {
		try {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
			channel.close().sync();
			logger.info(NettyNetworkServer.class.getSimpleName() + " shutdown.");
		} catch (Exception e) {
			throw new LinkException("NettyNetworkServer shutdown fail.", e);
		}
	}


}


