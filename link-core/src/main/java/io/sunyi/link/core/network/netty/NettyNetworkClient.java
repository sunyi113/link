package io.sunyi.link.core.network.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.sunyi.link.core.body.RpcRequest;
import io.sunyi.link.core.body.RpcResponse;
import io.sunyi.link.core.exception.LinkRuntimeException;
import io.sunyi.link.core.network.NetworkClient;
import io.sunyi.link.core.serialize.SerializeFactory;
import io.sunyi.link.core.serialize.hessian.HessianSerializeFactory;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author sunyi
 */
public class NettyNetworkClient implements NetworkClient {

	private static final Map<Long, SyncHolder> holderMap = new ConcurrentHashMap<Long, SyncHolder>();

	private volatile Channel channel = null;

	private volatile String hostAddress;
	private volatile int port;


	@Override
	public void connection(InetSocketAddress inetSocketAddress) {
		hostAddress = inetSocketAddress.getAddress().getHostAddress();
		port = inetSocketAddress.getPort();

		EventLoopGroup group = new NioEventLoopGroup();

		try {
			Bootstrap b = new Bootstrap();
			b.group(group)
					.channel(NioSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {

							ch.pipeline().addLast("LengthFieldPrepender", new LengthFieldPrepender(4));
							ch.pipeline().addLast("LengthFieldBasedFrameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));

							ch.pipeline().addLast("encode", new NettyEncode(getSerializeFactory()));
							ch.pipeline().addLast("decode", new NettyDecode(getSerializeFactory()));

							ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {

								@Override
								public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
									if (!(msg instanceof RpcResponse)) {
										// TODO delete sout
										System.out.println("msg instanceof RpcResponse == false");
										return;
									}

									RpcResponse response = (RpcResponse) msg;

									Long id = response.getId();
									SyncHolder holder = holderMap.get(id);
									if (holder == null) {
										// TODO delete sout
										System.out.println("Response timeout, id:[" + id + "]");
									}

									holder.rel.lock();

									try {
										holder.rpcResponse = response;
										holder.condition.signalAll();
									} finally {
										holder.rel.unlock();
									}
								}
							});
						}
					});


			ChannelFuture channelFuture = b.connect(hostAddress, port).sync();
			channel = channelFuture.channel();

		} catch (Exception e) {
			throw new LinkRuntimeException("Netty 连接服务器, 建立失败 hostAddress:[\" + hostAddress + \"],port:[\" + port + \"]", e);
		}
	}

	@Override
	public RpcResponse send(RpcRequest rpcRequest, Long sendTimeout, Long timeout) {


		SyncHolder holder = new SyncHolder();
		holder.rpcRequest = rpcRequest;

		ReentrantLock rel = new ReentrantLock();
		holder.rel = rel;

		Condition condition = rel.newCondition();
		holder.condition = condition;

		Long id = rpcRequest.getId();
		holderMap.put(id, holder);


		rel.lock();

		try {


			boolean isSend = channel.writeAndFlush(rpcRequest).await(sendTimeout, TimeUnit.MILLISECONDS);
			if (!isSend) {
				// 发送数据超时， 这种服务器应该是接收不到信息
				RpcResponse response = new RpcResponse();
				response.setHasException(true);
				response.setException(new LinkRuntimeException(LinkRuntimeException.SEND_TIMEOUT_ERROR, "Send message timeout, hostAddress:[" + hostAddress + "],port:[" + port + "]."));
				return response;
			}

			boolean isResponse = condition.await(timeout, TimeUnit.MILLISECONDS);
			if (!isResponse) {
				// 等待服务器响应超时
				RpcResponse response = new RpcResponse();
				response.setHasException(true);
				response.setException(new LinkRuntimeException(LinkRuntimeException.TIMEOUT_ERROR, "Waiting response timeout, hostAddress:[" + hostAddress + "],port:[" + port + "]."));
				return response;
			}


			RpcResponse rpcResponse = holder.rpcResponse;
			if (rpcResponse == null) {
				// 因为未知原因，造成的服务器响应没有收到, 这种情况应该不会出现
				RpcResponse response = new RpcResponse();
				response.setHasException(true);
				response.setException(new LinkRuntimeException("Not found the response, hostAddress:[" + hostAddress + "],port:[" + port + "]."));
				return response;
			}

			return rpcResponse;

		} catch (Exception e) {
			RpcResponse response = new RpcResponse();
			response.setHasException(true);
			response.setException(new LinkRuntimeException(e));
			return response;
		} finally {
			rel.unlock();
			holderMap.remove(id);
		}


	}

	@Override
	public SerializeFactory getSerializeFactory() {
		return HessianSerializeFactory.getInstance();
	}

	@Override
	public boolean isActive() {
		if (channel != null) {
			return channel.isActive();
		}

		return false;
	}

	@Override
	public void close() {
		if (channel != null) {
			channel.close();
		}

		// TODO 应该暂时先不把连接关闭， 需要等所有的请求都等到响应或者超时后，再关闭连接。
	}

	/**
	 * 因为请求-响应是个异步过程，
	 * 接收响应是在另外一个线程中，
	 * 需要某个东西把请求-响应的东西融合在一起。
	 */
	private static class SyncHolder {
		volatile RpcRequest rpcRequest;
		volatile ReentrantLock rel;
		volatile Condition condition;
		volatile RpcResponse rpcResponse;
	}


}
