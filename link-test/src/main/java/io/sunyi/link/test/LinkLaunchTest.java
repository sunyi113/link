package io.sunyi.link.test;

import io.sunyi.link.core.server.ServerBootstrap;
import io.sunyi.link.core.network.NetworkServer;
import io.sunyi.link.core.network.netty.NettyNetworkServer;
import io.sunyi.link.core.registry.Registry;
import io.sunyi.link.core.registry.zookeeper.ZookeeperRegistry;
import io.sunyi.link.core.server.ServerConfig;

/**
 * @author sunyi
 */
public class LinkLaunchTest {

	public static void main(String args[]) {


		NetworkServer networkServer = new NettyNetworkServer(10001);

		Registry registry = new ZookeeperRegistry("192.168.1.120:2181");


		ServerBootstrap serverBootstrap = ServerBootstrap.getInstance();

		serverBootstrap.setNetworkServer(networkServer);
		serverBootstrap.setRegistry(registry);

		serverBootstrap.start();

		ServerConfig<HelloService> serverConfig = new ServerConfig<HelloService>();
		serverConfig.setInterfaceClass(HelloService.class);
		serverConfig.setServerImplement(new HelloServiceImpl());


		serverBootstrap.exportServer(serverConfig);


	}

}
