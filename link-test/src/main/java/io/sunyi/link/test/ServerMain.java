package io.sunyi.link.test;

import io.sunyi.link.core.context.ApplicationContext;
import io.sunyi.link.core.serialize.hessian.HessianSerializeFactory;
import io.sunyi.link.core.server.ServerBootstrap;
import io.sunyi.link.core.network.NetworkServer;
import io.sunyi.link.core.network.netty.NettyNetworkServer;
import io.sunyi.link.core.registry.Registry;
import io.sunyi.link.core.registry.zookeeper.ZookeeperRegistry;
import io.sunyi.link.core.server.ServerConfig;
import io.sunyi.link.core.server.ServerReceivedHandler;

/**
 * @author sunyi
 */
public class ServerMain {

	public static void main(String args[]) {


		NetworkServer networkServer = new NettyNetworkServer(10001);

		Registry registry = new ZookeeperRegistry("192.168.1.120:2181");


		ApplicationContext.setNetworkServer(networkServer);
		ApplicationContext.setRegistry(registry);
		ApplicationContext.setSerializeFactory(HessianSerializeFactory.getInstance());
		ApplicationContext.setServerReceivedHandler(new ServerReceivedHandler());


		ServerBootstrap serverBootstrap = ServerBootstrap.getInstance();

		serverBootstrap.start();

		ServerConfig<HelloService> serverConfig = new ServerConfig<HelloService>();
		serverConfig.setInterfaceClass(HelloService.class);
		serverConfig.setServerImplement(new HelloServiceImpl());


		serverBootstrap.exportServer(serverConfig);


	}

}
