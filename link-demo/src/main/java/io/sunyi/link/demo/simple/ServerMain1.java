package io.sunyi.link.demo.simple;

import io.sunyi.link.core.body.RpcRequest;
import io.sunyi.link.core.body.RpcResponse;
import io.sunyi.link.core.commons.LinkApplicationContext;
import io.sunyi.link.core.filter.ServerFilter;
import io.sunyi.link.core.server.ServerBootstrap;
import io.sunyi.link.core.server.ServerConfig;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sunyi
 */
public class ServerMain1 {

	public static void main(String args[]) {


		LinkApplicationContext.setRegistryUrl("192.168.1.120:2181");
		LinkApplicationContext.setNetworkServerPort(10001);

		LinkApplicationContext.addServerFilters(new ServerFilter() {

			private AtomicInteger count = new AtomicInteger();

			@Override
			public void preInvoke(RpcRequest rpcRequest) {
				int i = count.incrementAndGet();
				if (i % 100 == 0) {
					System.out.println("ServerMain1 已经收到 " + i + " 次请求.....");
				}
			}

			@Override
			public void afterInvoke(RpcRequest rpcRequest, RpcResponse rpcResponse) {

			}
		});

		LinkApplicationContext.initialization();

		ServerBootstrap serverBootstrap = ServerBootstrap.getInstance();

		ServerConfig<HelloService> serverConfig = new ServerConfig<HelloService>();
		serverConfig.setInterfaceClass(HelloService.class);
		serverConfig.setServerImplement(new HelloServiceImpl());


		serverBootstrap.exportServer(serverConfig);


	}

}
